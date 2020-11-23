package com.example.progress.logic;

import androidx.annotation.NonNull;

public enum ExerciseType {
    Shoulders{
        @NonNull
        @Override
        public String toString() {
            return "Shoulders";
        }
    },
    Chest{
        @NonNull
        @Override
        public String toString() {
            return "Chest";
        }
    },
    Arms{
        @NonNull
        @Override
        public String toString() {
            return "Arms";
        }
    },
    Back{
        @NonNull
        @Override
        public String toString() {
            return "Back";
        }
    },
    Legs{
        @NonNull
        @Override
        public String toString() {
            return "Legs";
        }
    },
    Core{
        @NonNull
        @Override
        public String toString() {
            return "Core";
        }
    }
}
