package lexer_java;

public class Semantics {

	State M (Program P) {
		return M(p. body, initialState(p.decpart));
		// initialState >> Hashmap의로 Value 저장
	}
	
	public class State extends HashMap{
		// HashMap을 State에 상속
	}
	
	State initialState(Declarations d) {
		State state = new State();
		for (Declaration decl : d)
			state.put(decl.v, Value.mkValue(decl.t));
		return state;
	}
	// M : Statement X State -> State
	// M의 명세. M(Statement s, State state).
	// (Skip | Assignment | Conditional | Loop | Block)
	
	State M (Statement s, State state) {
        if (s instanceof Skip) return M((Skip)s, state);
        if (s instanceof Assignment)  return M((Assignment)s, state);
        if (s instanceof Block)  return M((Block)s, state);
        if (s instanceof Loop)  return M((Loop)s, state);
        if (s instanceof Conditional)  return M((Conditional)s, state);
        throw new IllegalArgumentException();
    }
    // Skip : 현재상태(State)가 그대로 유지.
	State M(Skip s, State state) {
		return state;
	}
	// Assignment : input state에 있는 target Variable에 source expression값을 저장
	State M(Assignment a, State state) {
		return state.onion(a.target, M(a.source, state));
		//onion은 따로 구현된 것으로 보임.
		
	}
	
	// Conditional : test is true = thenbranch / false = elsebranch
	State M(Conditional c, State state) {
        if (M(c.test, state).boolValue())
            return M (c.thenbranch, state);
        else
            return M (c.elsebranch, state);
    }
	
	// Block, Loop 
	// Loop : test is true = l.body / false state.
	State M (Loop l, State state) {
		if (M(l.test, state).boolValue())
			return M(l, M (l.body, state));
		else return state;
	}
	
	// Block : 코드에서의 중괄호를 처리해주는 부분.
    State M (Block b, State state) {
        for (Statement s : b.members)
            state = M (s, state);
        return state;
    }

	// Value M에서 사용할 applyBinary 함수
	Value applyBinary(Operator op, Value term1, Value term2) {
	    // op의 연산자 종류를 확인 후 계산 결과를 return. op가 '*'라면, term1 * term2의 값을 return 한다.
	    return (term1 op term2);
	
	}
	
	// applyUnary 함수선언
	Value applyUnary (Operator op, Value term) {
		// op의 연사자 종류를 확인 후 결과를 return. op가 '!'라면, !term의 값을 return 한다.
		return ( op term );
	}
	
	// Value : Expression과 State를 보아 값이 무엇인지 확인 후 반환
	Value M (Expression e, State state) {
        if (e instanceof Value) 
            return (Value)e;
        if (e instanceof Variable) 
            return (Value)(state.get(e));
        if (e instanceof Binary) {
            Binary b = (Binary)e;
            return applyBinary (b.op, M(b.term1, state), M(b.term2, state));
            // applyBinary : 각 term들을 op에 맞게 계산해주는 함수.      
        }
        // Unary에 대한 판단이 없었기 때문에 추가한 후, 해당하는 applyUnary도 사용해줌
        // applyUnary : 각 term들을 op에 맞게 적용해줌.
        if (e instanceof Unary) {
            Unary u = (Unary)e;
            return applyUnary(u.op, M(u.term, state));
        }
    }
}

// 2016125039 오상훈
