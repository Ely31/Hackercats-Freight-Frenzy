package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        // TODO: If you experience poor performance, enable this flag
        // System.setProperty("sun.java2d.opengl", "true");

        // Declare a MeepMeep instance
        // With a field size of 700 pixels at 50 fps
        MeepMeep meepMeep = new MeepMeep(740,50);

        RoadRunnerBotEntity bot = new DefaultBotBuilder(meepMeep)

                .setDimensions(12.6,18) // Width of 12.6 to match our thin bot
                .setConstraints(55, 45, Math.toRadians(167), Math.toRadians(167), 12)
                // The path we are simulating
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(11.4, -61,Math.toRadians(-90)))
                                .lineToSplineHeading(new Pose2d(-7.0, -45,Math.toRadians(-70)))
                                .build()
                );
                meepMeep
                        .setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                        // Set theme
                        .setTheme(new ColorSchemeRedDark())
                        // Background opacity from 0-1
                        .setBackgroundAlpha(1f)
                        .addEntity(bot)
                        .start();
    }
}