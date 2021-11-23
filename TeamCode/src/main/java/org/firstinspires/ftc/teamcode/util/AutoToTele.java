package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public class AutoToTele {

    public static Pose2d endOfAutoPose = new Pose2d();
    public static double endOfAutoHeading = 0;
    public static double zeroIsUpHeading = endOfAutoHeading-Math.toRadians(90);

}
