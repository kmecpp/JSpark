package com.kmecpp.jspark.compiler.parser.statement.statements;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kmecpp.jspark.JSpark;
import com.kmecpp.jspark.compiler.parser.Expression;
import com.kmecpp.jspark.compiler.parser.data.Type;
import com.kmecpp.jspark.compiler.parser.data.Variable;
import com.kmecpp.jspark.compiler.parser.statement.Import;
import com.kmecpp.jspark.compiler.parser.statement.Statement;
import com.kmecpp.jspark.compiler.parser.statement.block.AbstractBlock;
import com.kmecpp.jspark.compiler.parser.statement.block.module.Module;
import com.kmecpp.jspark.language.Keyword;

public class MethodInvocation extends Statement {

	//	private AbstractBlock block;
	private Variable capture;

	private String target;
	private String method;
	private ArrayList<Expression> params;

	public MethodInvocation(AbstractBlock block, String target, String method, ArrayList<Expression> params) {
		this(block, null, target, method, params);
	}

	public MethodInvocation(AbstractBlock block, Variable capture, String target, String method, ArrayList<Expression> params) {
		super(block);
		//		this.block = block;
		this.capture = capture;
		this.target = target;
		this.method = method;
		this.params = params;
	}

	public AbstractBlock getBlock() {
		return block;
	}

	public Variable getCapture() {
		return capture;
	}

	public String getTarget() {
		return target;
	}

	public String getMethod() {
		return method;
	}

	public ArrayList<Expression> getParams() {
		return params;
	}

	//	@Override
	//	public final void execute() {
	//		invoke();
	//	}

	@Override
	public void execute() {
		Object target = Keyword.THIS.is(this.target) ? block.getModule() : block.getVariable(this.target);

		Variable var = block.getVariable(this.target);
		if (var != null) {
			Object obj = var.getValue();
			try {
				invokeMethod(obj.getClass(), obj);
			} catch (Exception e) {
				System.err.println("Could not invoke method: " + toString());
				e.printStackTrace();
			}
		}

		if (target == null) {
			Optional<Import> imprt = block.getModule().getImport(this.target);
			if (imprt.isPresent()) {
				Module module = JSpark.getRuntime().getModule(imprt.get().getClassName());
				module.execute();
			} else {
				try {
					invokeMethod(Class.forName("com.kmecpp.jspark.language.builtin." + this.target), null);
				} catch (Exception e) {
					System.err.println("Could invoke static method: " + toString());
					e.printStackTrace();
				}

			}

			//			else if (this.target.equals("Console")) {
			//				if (this.method.equals("println")) {
			//					Console.println(params.get(0).evaluate());
			//				}
			//			} else {
			//
			//			}

		} else if (target instanceof Module) {
			Type[] paramTypes = new Type[params.size()];
			for (int i = 0; i < paramTypes.length; i++) {
				paramTypes[i] = params.get(i).getReturnType();
			}
			((Module) target).getMethod(method, paramTypes).get().invoke(params);
		}
	}

	private void invokeMethod(Class<?> cls, Object target) throws Exception {
		Object[] values = new Object[params.size()];

		methodSearch: for (java.lang.reflect.Method method : cls.getMethods()) {
			Class<?>[] targetTypes = method.getParameterTypes();
			if (targetTypes.length != values.length || !method.getName().equals(this.method)) {
				continue;
			}

			for (int i = 0; i < targetTypes.length; i++) {
				Object value = params.get(i).evaluate();
				if (value == null || targetTypes[i].isAssignableFrom(value.getClass())) {
					values[i] = value;
				} else {
					continue methodSearch;
				}
			}

			Object result = method.invoke(target, values);
			if (capture != null) {

				capture.setValue(result);
			}
			return;
		}
		throw new NoSuchMethodException();
	}

	@Override
	public String toString() {
		return toJavaCode();
	}

	@Override
	public String toJavaCode() {
		if (target.equals("Console") && method.equals("println")) {
			return "System.out.println(" + params.stream().map(String::valueOf).collect(Collectors.joining(" ")) + ")";
		}
		return target + "." + method + "(" + params.stream().map(String::valueOf).collect(Collectors.joining(" ")) + ")";
	}

}
