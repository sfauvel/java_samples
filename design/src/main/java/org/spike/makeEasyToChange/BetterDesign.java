package org.spike.makeEasyToChange;


/**
 * From "Refactoring is stupid"
 * Problem: What  happen if we want use another lib to read tension ?
 *
 * We search to make a design easy to change.
 */
public class BetterDesign {
    private Voltmeter voltmeter = new Voltmeter();
    private Ammeter ammeter = new Ammeter();

    public Resistance computeElectricalResistance() {
        ParticularElectricApi particularElectricApi = new ParticularElectricApi();
        Tension tension = voltmeter.readTension();
        Current current = ammeter.readCurrent();
        return Resistance.createUsingOhmLaw(tension, current);
    }

    static class Voltmeter {


        public Tension readTension() {
            return new Tension();
        }
    }

    static class Ammeter {

        public Current readCurrent() {
            return new Current();
        }
    }


    static class Tension {

    }

    static class Current {

    }

    static class Resistance {

        public static Resistance createUsingOhmLaw(Tension tension, Current current) {
            return new Resistance();
        }
    }
}
