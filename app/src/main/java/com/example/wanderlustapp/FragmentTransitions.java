package com.example.wanderlustapp;

import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionSet;

public class FragmentTransitions {

    public static Transition createEnterTransition() {
        TransitionSet enterTransition = new TransitionSet();
        enterTransition.addTransition(new Slide());
        enterTransition.addTransition(new Fade());
        return enterTransition;
    }

    public static Transition createExitTransition() {
        TransitionSet exitTransition = new TransitionSet();
        exitTransition.addTransition(new Slide());
        exitTransition.addTransition(new Fade());
        return exitTransition;
    }
}
