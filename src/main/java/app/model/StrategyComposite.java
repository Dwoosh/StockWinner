package app.model;

import app.exceptions.InvalidConditionException;
import app.exceptions.NoValidDateFoundException;

import java.util.ArrayList;

public class StrategyComposite implements IStrategyComponent {

    private ArrayList<IStrategyComponent> strategies;
    private StrategyEnums.Conditions condition;

    public StrategyComposite(IStrategyComponent strategyComponent){
        this.strategies = new ArrayList<>();
        this.strategies.add(strategyComponent);
    }

    public void addStrategy(IStrategyComponent strategy){
        strategies.add(strategy);
    }

    public void removeStrategy(IStrategyComponent strategy){
        strategies.remove(strategy);
    }

    public void setCondition(StrategyEnums.Conditions condition){
        this.condition = condition;
    }

    public StrategyEnums.Conditions getCondition() {
        return condition;
    }

    public boolean evaluate() throws NoValidDateFoundException, InvalidConditionException {
        boolean result = strategies.get(0).evaluate();
        for(int i = 1; i < strategies.size(); ++i){
            IStrategyComponent strategy = strategies.get(i);
            switch (this.condition){
                case AND:
                    result = result && strategy.evaluate();
                    break;
                case OR:
                    result = result || strategy.evaluate();
                    break;
                case NONE:
                    throw new InvalidConditionException();
            }
        }
        return result;
    }
}
