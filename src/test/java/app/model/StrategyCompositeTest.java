package app.model;

import app.exceptions.InvalidConditionException;
import org.junit.Test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StrategyCompositeTest {

    @Test
    public void testEvaluateWithInvalidCondition(){
        //given
        Strategy firstStrategy = mock(Strategy.class);
        Strategy secondStrategy = mock(Strategy.class);
        StrategyComposite strategyComposite = new StrategyComposite(firstStrategy);
        strategyComposite.addStrategy(secondStrategy);

        try {
            //when
            when(firstStrategy.evaluate()).thenReturn(true);
            when(secondStrategy.evaluate()).thenReturn(true);
            when(firstStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.NONE);
            when(secondStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.NONE);

            //then
            strategyComposite.evaluate();
        }
        catch (Exception ex){
            assertTrue(ex instanceof InvalidConditionException);
        }
    }

    @Test
    public void testEvaluateWithSingleStrategy(){
        //given
        Strategy firstStrategy = mock(Strategy.class);
        StrategyComposite strategyComposite = new StrategyComposite(firstStrategy);

        try {
            //when
            when(firstStrategy.evaluate()).thenReturn(true);
            when(firstStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.NONE);

            //then
            assertTrue(strategyComposite.evaluate());
        }
        catch (Exception ex){ }
    }

    @Test
    public void testEvaluateWithMultipleAndTrueStrategy(){
        //given
        Strategy firstStrategy = mock(Strategy.class);
        Strategy secondStrategy = mock(Strategy.class);
        Strategy thirdStrategy = mock(Strategy.class);
        StrategyComposite strategyComposite = new StrategyComposite(firstStrategy);
        strategyComposite.addStrategy(secondStrategy);
        strategyComposite.addStrategy(thirdStrategy);

        try {
            //when
            when(firstStrategy.evaluate()).thenReturn(true);
            when(secondStrategy.evaluate()).thenReturn(true);
            when(thirdStrategy.evaluate()).thenReturn(true);
            when(firstStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.NONE);
            when(secondStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.AND);
            when(thirdStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.AND);

            //then
            assertTrue(strategyComposite.evaluate());
        }
        catch (Exception ex){ }
    }

    @Test
    public void testEvaluateWithMultipleAndFalseStrategy(){
        //given
        Strategy firstStrategy = mock(Strategy.class);
        Strategy secondStrategy = mock(Strategy.class);
        Strategy thirdStrategy = mock(Strategy.class);
        StrategyComposite strategyComposite = new StrategyComposite(firstStrategy);
        strategyComposite.addStrategy(secondStrategy);
        strategyComposite.addStrategy(thirdStrategy);

        try {
            //when
            when(firstStrategy.evaluate()).thenReturn(true);
            when(secondStrategy.evaluate()).thenReturn(false);
            when(thirdStrategy.evaluate()).thenReturn(true);
            when(firstStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.NONE);
            when(secondStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.AND);
            when(thirdStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.AND);

            //then
            assertFalse(strategyComposite.evaluate());
        }
        catch (Exception ex){ }
    }

    @Test
    public void testEvaluateWithMultipleOrTrueStrategy(){
        //given
        Strategy firstStrategy = mock(Strategy.class);
        Strategy secondStrategy = mock(Strategy.class);
        Strategy thirdStrategy = mock(Strategy.class);
        StrategyComposite strategyComposite = new StrategyComposite(firstStrategy);
        strategyComposite.addStrategy(secondStrategy);
        strategyComposite.addStrategy(thirdStrategy);

        try {
            //when
            when(firstStrategy.evaluate()).thenReturn(true);
            when(secondStrategy.evaluate()).thenReturn(false);
            when(thirdStrategy.evaluate()).thenReturn(true);
            when(firstStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.NONE);
            when(secondStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.OR);
            when(thirdStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.OR);

            //then
            assertTrue(strategyComposite.evaluate());
        }
        catch (Exception ex){ }
    }

    @Test
    public void testEvaluateWithMultipleOrFalseStrategy(){
        //given
        Strategy firstStrategy = mock(Strategy.class);
        Strategy secondStrategy = mock(Strategy.class);
        Strategy thirdStrategy = mock(Strategy.class);
        StrategyComposite strategyComposite = new StrategyComposite(firstStrategy);
        strategyComposite.addStrategy(secondStrategy);
        strategyComposite.addStrategy(thirdStrategy);

        try {
            //when
            when(firstStrategy.evaluate()).thenReturn(false);
            when(secondStrategy.evaluate()).thenReturn(false);
            when(thirdStrategy.evaluate()).thenReturn(false);
            when(firstStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.NONE);
            when(secondStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.OR);
            when(thirdStrategy.getCondition()).thenReturn(StrategyEnums.Conditions.OR);

            //then
            assertFalse(strategyComposite.evaluate());
        }
        catch (Exception ex){ }
    }

}
