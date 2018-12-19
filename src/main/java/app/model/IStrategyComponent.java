package app.model;

import app.exceptions.InvalidConditionException;
import app.exceptions.NoValidDateFoundException;

public interface IStrategyComponent {

    boolean evaluate() throws NoValidDateFoundException, InvalidConditionException;

    StrategyEnums.Conditions getCondition();

}
