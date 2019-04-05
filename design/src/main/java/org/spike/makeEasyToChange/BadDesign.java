package org.spike.makeEasyToChange;

/**
 * From "Refactoring is stupid": https://medium.com/thomas-benard/refactoring-is-stupid-454ba11c31e1
 * Problems:
 * - static dependency issues.
 * - some primitive obsession.
 * <p>
 * Problem: What  happen if we want use another lib to read tension ?
 */
public class BadDesign {
    public Double computeElectricalResistance() {
        ParticularElectricApi particularElectricApi = new ParticularElectricApi();
        double tension = particularElectricApi.readTension();
        double current = particularElectricApi.readCurrent();
        return tension / current;
    }



    // If we want to use a different Ammeter.
    boolean useTeslaWasBetterThanEdison = true;

    class TeslaWasBetterThanEdison {

        public double current() {
            return 0.0;
        }
    }

    public double computeElectricalResistanceWithTesla() {
        ParticularElectricApi particularElectricApi = new ParticularElectricApi();
        double tension = particularElectricApi.readTension();
        double current;
        if (useTeslaWasBetterThanEdison) {
            TeslaWasBetterThanEdison teslaWasBetterThanEdison = new TeslaWasBetterThanEdison();
            current = teslaWasBetterThanEdison.current();
        } else {
            current = particularElectricApi.readCurrent();
        }
        return tension / current;
    }

}
