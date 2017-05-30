package pro1a.ics.upjs.sk.the_heck.help;

import pro1a.ics.upjs.sk.the_heck.R;

public class SpecializationIcon {

    public static int getIconNumber(String specialization) {
        int iconNumber;
        switch (specialization) {
            case "Allergist":
                iconNumber = R.mipmap.allergist;
                break;
            case "Cardiologist":
                iconNumber = R.mipmap.cardiologist;
                break;
            case "Dentist":
                iconNumber = R.mipmap.dentist;
                break;
            case "Dermatologist":
                iconNumber = R.mipmap.dermatologist;
                break;
            case "Endocrinologist":
                iconNumber = R.mipmap.endocrinologist;
                break;
            case "Gastroenterologist":
                iconNumber = R.mipmap.gastroenterologist;
                break;
            case "Gynecologist":
                iconNumber = R.mipmap.gynecologist;
                break;
            case "Hematologist":
                iconNumber = R.mipmap.hematologist;
                break;
            case "Neurologist":
                iconNumber = R.mipmap.neurologist;
                break;
            case "Ophthalmologist":
                iconNumber = R.mipmap.ophthalmologist;
                break;
            case "Orthopedist":
                iconNumber = R.mipmap.orthopedist;
                break;
            case "Pediatrician":
                iconNumber = R.mipmap.pediatrician;
                break;
            case "Periodontist":
                iconNumber = R.mipmap.periodontist;
                break;
            case "Radiologist":
                iconNumber = R.mipmap.radiologist;
                break;
            case "Urologist":
                iconNumber = R.mipmap.urologist;
                break;
            default:
                iconNumber = R.mipmap.hospital;
                break;
        }

        return iconNumber;
    }
}
