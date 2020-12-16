package com.endava.statemachine;


import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

@SpringBootApplication
public class StatemachineSerializationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatemachineSerializationApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
            InMemoryStateMachinePersist stateMachinePersist = new InMemoryStateMachinePersist();
            StateMachinePersister<States, Transitions, String> persister = new DefaultStateMachinePersister<>(stateMachinePersist);
            StateMachine<States, Transitions> stateMachine1 = context.getBean("machine1", StateMachine.class);
            StateMachine<States, Transitions> stateMachine2 = context.getBean("machine2", StateMachine.class);
            stateMachine1.start();
            stateMachine1.sendEvent(Transitions.T1);
            stateMachine1.getExtendedState().getVariables().put("mike", new User("Mike", "Smith"));
            System.out.println(stateMachine1.getState().getIds());
            persister.persist(stateMachine1, "myid");
            persister.restore(stateMachine2, "myid");
            System.out.println(stateMachine2.getState().getIds());
        };
    }
}

class User {
    private final String firstName;
    private final String lastName;

    User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

class InMemoryStateMachinePersist implements StateMachinePersist<States, Transitions, String> {

    private String savedContext;

    @Override
    public void write(StateMachineContext<States, Transitions> context, String contextOjb) throws Exception {
        System.out.println();
        String json = toJson(context);
        System.out.println(json);
        savedContext = json;
    }

    private String toJson(Object instance) {
        return JsonWriter.objectToJson(instance);
    }

    @Override
    public StateMachineContext<States, Transitions> read(String contextOjb) throws Exception {
        return (StateMachineContext<States, Transitions>) JsonReader.jsonToJava(savedContext);
    }
}
