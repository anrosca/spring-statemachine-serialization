package com.endava.statemachine;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "machine1")
class Config1 extends Config {
}

@Configuration
@EnableStateMachine(name = "machine2")
class Config2 extends Config {
}

class Config extends StateMachineConfigurerAdapter<States, Transitions> {
    @Override
    public void configure(StateMachineConfigBuilder<States, Transitions> config) throws Exception {
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Transitions> states) throws Exception {
        states
                .withStates()
                .initial(States.S1)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Transitions> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.S1).target(States.S2)
                .event(Transitions.T1)
                .and()
                .withInternal()
                .source(States.S2)
                .event(Transitions.T2)
                .and()
                .withLocal()
                .source(States.S2).target(States.S3)
                .event(Transitions.T3);
    }
}

enum States {
    S1, S2, S3
}

enum Transitions {
    T1, T2, T3
}
