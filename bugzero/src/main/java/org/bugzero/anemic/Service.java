package org.bugzero.anemic;

public class Service {

    public void execute(Instruction instruction) {

        instruction.setFlag("FR");

        // Need to setFlag before setting User
        // It break if we are doing this treatement before setting flag.
        if (instruction.getFlag()=="FR") {
            instruction.setUser("Robert");
        } else {
            instruction.setUser("Bob");
        }
    }


    class RichInstruction {
        private final String flag;
        private final String user;

        public RichInstruction(String flag) {
            this.flag = flag;
            if (flag=="FR") {
                user = "Robert";
            } else {
                user = "Bob";
            }
        }

        public String getFlag() {
            return flag;
        }

        public String getUser() {
            return user;
        }

    }

    public void execute2() {
        new RichInstruction("FR");
    }


}
