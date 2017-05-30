package pro1a.ics.upjs.sk.the_heck.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.KEY_IS_LOGGED_IN;
import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREF_NAME;

public class SessionManager {
    SharedPreferences sharedPreferences;
    Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
