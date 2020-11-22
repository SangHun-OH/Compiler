package compiler2;
// SemanticAnalyzer.java

import java.util.*;

public class SemanticAnalyzer {

	public static SymbolTable typing(Declarations d) {
		SymbolTable map = new SymbolTable();
		for (Declaration di : d)
			map.put(di.v, di.t);
		return map;
	}

	public static void check(boolean test, String msg) {
		if (test)
			return;
		System.err.println(msg);
		System.exit(1);
	}

	public static void V(Declarations d) {
		for (int i = 0; i < d.size() - 1; i++)
			for (int j = i + 1; j < d.size(); j++) {
				Declaration di = d.get(i);
				Declaration dj = d.get(j);
				check(!(di.v.equals(dj.v)), "duplicate declaration: " + dj.v);
			}
	}

	public static void V(Program p) {
		V(p.decpart);
		V(p.body, typing(p.decpart));
	}

	public static Type typeOf(Expression e, SymbolTable tm) {
		if (e instanceof Value)
			return ((Value) e).type;
		if (e instanceof Variable) {
			Variable v = (Variable) e;
			check(tm.containsKey(v), "undefined variable: " + v);
			return (Type) tm.get(v);
		}
		if (e instanceof Binary) {
			Binary b = (Binary) e;
			if (b.op.ArithmeticOp())
				if (typeOf(b.term1, tm) == Type.FLOAT)
					return (Type.FLOAT);
				else
					return (Type.INT);
			if (b.op.RelationalOp() || b.op.BooleanOp())
				return (Type.BOOL);
		}
		if (e instanceof Unary) {
			Unary u = (Unary) e;
			if (u.op.NotOp())
				return (Type.BOOL);
			else if (u.op.NegateOp())
				return typeOf(u.term, tm);
			else if (u.op.intOp())
				return (Type.INT);
			else if (u.op.floatOp())
				return (Type.FLOAT);
			else if (u.op.charOp())
				return (Type.CHAR);
		}
		throw new IllegalArgumentException("should never reach here");
	}

	public static void V(Expression e, SymbolTable tm) {
		if (e instanceof Value)
			return;
		if (e instanceof Variable) {
			Variable v = (Variable) e;
			check(tm.containsKey(v), "undeclared variable: " + v);
			return;
		}
		if (e instanceof Binary) {
			Binary b = (Binary) e;
			Type typ1 = typeOf(b.term1, tm);
			Type typ2 = typeOf(b.term2, tm);
			V(b.term1, tm);
			V(b.term2, tm);
			if (b.op.ArithmeticOp())
				check(typ1 == typ2 && (typ1 == Type.INT || typ1 == Type.FLOAT), "type error for " + b.op);
			else if (b.op.RelationalOp())
				check(typ1 == typ2, "type error for " + b.op);
			else if (b.op.BooleanOp())
				check(typ1 == Type.BOOL && typ2 == Type.BOOL, b.op + ": non-bool operand");
			else
				throw new IllegalArgumentException("should never reach here");
			return;
		}

		// add code here

		if (e instanceof Unary) {
			Unary u = (Unary) e;
			Type t = typeOf(u.term, tm);
			V(u.term, tm);
			if (u.op.NotOp()) {
				check(t.equals(Type.BOOL), "에러 : " + u.op);
			} else if (u.op.NegateOp()) {
				check(t.equals(Type.FLOAT) || t.equals(Type.INT), "에러 : " + u.op);
			} else if (u.op.floatOp()) {
				check(t.equals(Type.INT), "에러 : " + u.op);
			} else if (u.op.charOp()) {
				check(t.equals(Type.INT), "에러 : " + u.op);
			} else if (u.op.intOp()) {
				check(t.equals(Type.FLOAT) || t.equals(Type.CHAR), "에러 : " + u.op);
			} else
				throw new IllegalArgumentException("다른 unary에 도달");
			return;
		}

		//

		throw new IllegalArgumentException("should never reach here");
	}

	public static void V(Statement s, SymbolTable tm) {
		if (s == null)
			throw new IllegalArgumentException("AST error: null statement");
		if (s instanceof Skip)
			return;
		if (s instanceof Assignment) {
			Assignment a = (Assignment) s;
			check(tm.containsKey(a.target), " undefined target in assignment: " + a.target);
			V(a.source, tm);
			Type ttype = (Type) tm.get(a.target);
			Type srctype = typeOf(a.source, tm);
			if (ttype != srctype) {
				if (ttype == Type.FLOAT)
					check(srctype == Type.INT, "mixed mode assignment to " + a.target);
				else if (ttype == Type.INT)
					check(srctype == Type.CHAR, "mixed mode assignment to " + a.target);
				else
					check(false, "mixed mode assignment to " + a.target);
			}
			return;
		}

		// add code here

		if (s instanceof Conditional) {
			Conditional c = (Conditional) s;
			V(c.test, tm);
			check(typeOf(c.test, tm).equals(Type.BOOL), "conditional 에러  " + c.test);
			V(c.thenbranch, tm);
			V(c.elsebranch, tm);
			return;
		}
		if (s instanceof Loop) {
			Loop l = (Loop) s;
			V(l.test, tm);
			check(typeOf(l.test, tm).equals(Type.BOOL), "loop 에러  " + l.test);
			V(l.body, tm);
			return;
		}
		if (s instanceof Block) {
			Block b = (Block) s;
			if (b.members.size() == 0)
				return;
			for (int i = 0; i < b.members.size(); i++) {
				V(b.members.get(i), tm);
			}
			return;
		}

		// add code

		throw new IllegalArgumentException("should never reach here");
	}

	public static void main(String args[]) {
		Parser parser = new Parser(new Lexer("p4.cl"));
		Program prog = parser.program();
		// prog.display();

		System.out.println("\nSemantic Analysis...");
		V(prog); // 의미분석기

	} // main

} // class SemanticAnalyzer
//오류 : p2, p4               정상 : p1, p3
