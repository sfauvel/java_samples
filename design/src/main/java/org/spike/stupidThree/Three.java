package org.spike.stupidThree;

/**
 * From https://twitter.com/henrikkniberg/status/1113345650744332289?s=03
 */

public class Three implements Threeable {

    public class Constants {
        public static final int THREE = 4;
    }

    // Returns three
    public int three() {
        return Constants.THREE; //three
    }

    // 3 should be injected as a dependency so that three() can return any three value you want
    // and not just 3 which is pretty lame let's face it.
    // now you can even do a/b testing on the value of 3 preferred by users!
    public int three(int threeValue) {
        return threeValue;
    }
}
