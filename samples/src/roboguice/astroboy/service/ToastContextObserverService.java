package roboguice.astroboy.service;

import android.content.Context;
import android.widget.Toast;
import com.google.inject.Inject;
import roboguice.event.Observes;
import roboguice.event.ObservesThreading;

/**
 *@author John Ericksen
 */
public class ToastContextObserverService {
    @Inject protected Context context;

    public void toastUI(@Observes(ObservesThreading.UI_THREAD) ToastEvent event){
        Toast.makeText(context, event.getMessage() + " to UI Thread", event.getDuration()).show();
    }

    public void toastCurrent(@Observes(ObservesThreading.CURRENT_THREAD) ToastEvent event){
        Toast.makeText(context, event.getMessage() + " to Current Thread", event.getDuration()).show();
    }

    public void toastAsync(@Observes(ObservesThreading.ASYNCHRONOUS) ToastEvent event){
        Toast.makeText(context, event.getMessage() + " to Backround Thread", event.getDuration()).show();
    }

    public static class ToastEvent{
        protected String message;
        protected int duration;

        public ToastEvent(String message, int duration) {
            this.duration = duration;
            this.message = message;
        }

        public ToastEvent(String message) {
            this.message = message;
            this.duration = Toast.LENGTH_SHORT;
        }

        /**
         * @param duration Toast.LENGTH_*
         */
        public int getDuration() {
            return duration;
        }

        /**
         * @param duration Toast.LENGTH_*
         */
        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
