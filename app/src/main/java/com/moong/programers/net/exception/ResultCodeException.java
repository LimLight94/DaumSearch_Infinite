package com.moong.programers.net.exception;


import com.moong.programers.base.impl.NonActivityInterface;
import com.moong.programers.data.Res;

/**
 * Appg-EzBalance
 * Class: ResultCodeException
 * Created by d on 2019-09-30.
 * <p>
 * Description:
 */
public class ResultCodeException extends Exception implements NonActivityInterface {
    private Res mRes = null;

    public ResultCodeException(String message) {
        super(message);
    }

    public ResultCodeException(Res res, String message) {
        super(message);
        this.mRes = res;
    }

    public static ResultCodeException create(Res res, boolean showPopup) {
        if (res == null) {
            return new ResultCodeException("Error on Communicate");
        }

        int rcode = res.getStatusCode();
        String message = "오류 발생 ";
        switch (rcode){
            case 400:
                message += ": 클라이언트 요청오류";
                break;
            case 404:
                message += ": 조회한 데이터 없음";
                break;
            case 500:
                message += ": 서버 오류";
                break;
        }
//        String message = String.format("Error on Communicate. rcode = %s, rmsg = %s", rcode, rmsg);

//        if (showPopup) {
//            Activity activity = ActivityReference.getActivtyReference();
//            if (activity == null) return new ResultCodeException(message);
//            if (TextUtils.isEmpty(rmsg)) return new ResultCodeException(message);
//
//            RichUtils.runOnUiThread(() -> new AlertDialog.Builder(activity)
//                    .setMessage(rmsg)
//                    .setPositiveButton(android.R.string.ok, null)
//                    .show());
//        }

        return new ResultCodeException(res, message);
    }

    public Res getRes() {
        return mRes;
    }
}
