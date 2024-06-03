// Utils.java
package com.example.supporty.goal;

import java.util.Random;
import java.util.Date;

public class Utils {
    public static int generateRandomGoalId() {
        Random random = new Random();
        int randomGoalId = random.nextInt(10000000);
        return randomGoalId;
    }

    public static int generateRandomVisitId() {
        Random random = new Random();
        int randomVisitId = random.nextInt(10000000);
        return randomVisitId;
    }

    public static Date generateCurrentDate() {
        Date currentDate = new Date();
        return currentDate;
    }
}
