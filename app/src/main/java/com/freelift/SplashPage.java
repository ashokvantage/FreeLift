package com.freelift;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SplashPage extends AppCompatActivity implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    RelativeLayout rl_login_register, rl_login, rl_register;
    String Login_Type = "driver";
    Long customer_driver_login_id;
    public static String Androi_Id;
    SliderLayout sliderLayout;
    HashMap<String, Integer> Hash_file_maps;
    PagerIndicator indicator;
    public static boolean click_on_notification = false;
    String refreshedToken = "";
    public static String country_code = "{countries: [{" +
            "        name: \"Afghanistan\",\n" +
            "        cca2: \"af\",\n" +
            "        \"calling-code\": \"93\"\n" +
            "    }, {\n" +
            "        name: \"Albania\",\n" +
            "        cca2: \"al\",\n" +
            "        \"calling-code\": \"355\"\n" +
            "    }, {\n" +
            "        name: \"Algeria\",\n" +
            "        cca2: \"dz\",\n" +
            "        \"calling-code\": \"213\"\n" +
            "    }, {\n" +
            "        name: \"American Samoa\",\n" +
            "        cca2: \"as\",\n" +
            "        \"calling-code\": \"1684\"\n" +
            "    }, {\n" +
            "        name: \"Andorra\",\n" +
            "        cca2: \"ad\",\n" +
            "        \"calling-code\": \"376\"\n" +
            "    }, {\n" +
            "        name: \"Angola\",\n" +
            "        cca2: \"ao\",\n" +
            "        \"calling-code\": \"244\"\n" +
            "    }, {\n" +
            "        name: \"Anguilla\",\n" +
            "        cca2: \"ai\",\n" +
            "        \"calling-code\": \"1264\"\n" +
            "    }, {\n" +
            "        name: \"Antigua and Barbuda\",\n" +
            "        cca2: \"ag\",\n" +
            "        \"calling-code\": \"1268\"\n" +
            "    }, {\n" +
            "        name: \"Argentina\",\n" +
            "        cca2: \"ar\",\n" +
            "        \"calling-code\": \"54\"\n" +
            "    }, {\n" +
            "        name: \"Armenia\",\n" +
            "        cca2: \"am\",\n" +
            "        \"calling-code\": \"374\"\n" +
            "    }, {\n" +
            "        name: \"Aruba\",\n" +
            "        cca2: \"aw\",\n" +
            "        \"calling-code\": \"297\"\n" +
            "    }, {\n" +
            "        name: \"Australia\",\n" +
            "        cca2: \"au\",\n" +
            "        \"calling-code\": \"61\"\n" +
            "    }, {\n" +
            "        name: \"Austria\",\n" +
            "        cca2: \"at\",\n" +
            "        \"calling-code\": \"43\"\n" +
            "    }, {\n" +
            "        name: \"Azerbaijan\",\n" +
            "        cca2: \"az\",\n" +
            "        \"calling-code\": \"994\"\n" +
            "    }, {\n" +
            "        name: \"Bahamas\",\n" +
            "        cca2: \"bs\",\n" +
            "        \"calling-code\": \"1242\"\n" +
            "    }, {\n" +
            "        name: \"Bahrain\",\n" +
            "        cca2: \"bh\",\n" +
            "        \"calling-code\": \"973\"\n" +
            "    }, {\n" +
            "        name: \"Bangladesh\",\n" +
            "        cca2: \"bd\",\n" +
            "        \"calling-code\": \"880\"\n" +
            "    }, {\n" +
            "        name: \"Barbados\",\n" +
            "        cca2: \"bb\",\n" +
            "        \"calling-code\": \"1246\"\n" +
            "    }, {\n" +
            "        name: \"Belarus\",\n" +
            "        cca2: \"by\",\n" +
            "        \"calling-code\": \"375\"\n" +
            "    }, {\n" +
            "        name: \"Belgium\",\n" +
            "        cca2: \"be\",\n" +
            "        \"calling-code\": \"32\"\n" +
            "    }, {\n" +
            "        name: \"Belize\",\n" +
            "        cca2: \"bz\",\n" +
            "        \"calling-code\": \"501\"\n" +
            "    }, {\n" +
            "        name: \"Benin\",\n" +
            "        cca2: \"bj\",\n" +
            "        \"calling-code\": \"229\"\n" +
            "    }, {\n" +
            "        name: \"Bermuda\",\n" +
            "        cca2: \"bm\",\n" +
            "        \"calling-code\": \"1441\"\n" +
            "    }, {\n" +
            "        name: \"Bhutan\",\n" +
            "        cca2: \"bt\",\n" +
            "        \"calling-code\": \"975\"\n" +
            "    }, {\n" +
            "        name: \"Bolivia\",\n" +
            "        cca2: \"bo\",\n" +
            "        \"calling-code\": \"591\"\n" +
            "    }, {\n" +
            "        name: \"Bosnia and Herzegovina\",\n" +
            "        cca2: \"ba\",\n" +
            "        \"calling-code\": \"387\"\n" +
            "    }, {\n" +
            "        name: \"Botswana\",\n" +
            "        cca2: \"bw\",\n" +
            "        \"calling-code\": \"267\"\n" +
            "    }, {\n" +
            "        name: \"Brazil\",\n" +
            "        cca2: \"br\",\n" +
            "        \"calling-code\": \"55\"\n" +
            "    }, {\n" +
            "        name: \"Brunei Darussalam\",\n" +
            "        cca2: \"bn\",\n" +
            "        \"calling-code\": \"673\"\n" +
            "    }, {\n" +
            "        name: \"Bulgaria\",\n" +
            "        cca2: \"bg\",\n" +
            "        \"calling-code\": \"359\"\n" +
            "    }, {\n" +
            "        name: \"Burkina Faso\",\n" +
            "        cca2: \"bf\",\n" +
            "        \"calling-code\": \"226\"\n" +
            "    }, {\n" +
            "        name: \"Burundi\",\n" +
            "        cca2: \"bi\",\n" +
            "        \"calling-code\": \"257\"\n" +
            "    }, {\n" +
            "        name: \"Cambodia\",\n" +
            "        cca2: \"kh\",\n" +
            "        \"calling-code\": \"855\"\n" +
            "    }, {\n" +
            "        name: \"Cameroon\",\n" +
            "        cca2: \"cm\",\n" +
            "        \"calling-code\": \"237\"\n" +
            "    }, {\n" +
            "        name: \"Canada\",\n" +
            "        cca2: \"ca\",\n" +
            "        \"calling-code\": \"1\"\n" +
            "    }, {\n" +
            "        name: \"Cape Verde\",\n" +
            "        cca2: \"cv\",\n" +
            "        \"calling-code\": \"238\"\n" +
            "    }, {\n" +
            "        name: \"Cayman Islands\",\n" +
            "        cca2: \"ky\",\n" +
            "        \"calling-code\": \"1345\"\n" +
            "    }, {\n" +
            "        name: \"Central African Republic\",\n" +
            "        cca2: \"cf\",\n" +
            "        \"calling-code\": \"236\"\n" +
            "    }, {\n" +
            "        name: \"Chad\",\n" +
            "        cca2: \"td\",\n" +
            "        \"calling-code\": \"235\"\n" +
            "    }, {\n" +
            "        name: \"Chile\",\n" +
            "        cca2: \"cl\",\n" +
            "        \"calling-code\": \"56\"\n" +
            "    }, {\n" +
            "        name: \"China\",\n" +
            "        cca2: \"cn\",\n" +
            "        \"calling-code\": \"86\"\n" +
            "    }, {\n" +
            "        name: \"Colombia\",\n" +
            "        cca2: \"co\",\n" +
            "        \"calling-code\": \"57\"\n" +
            "    }, {\n" +
            "        name: \"Comoros\",\n" +
            "        cca2: \"km\",\n" +
            "        \"calling-code\": \"269\"\n" +
            "    }, {\n" +
            "        name: \"Congo (DRC)\",\n" +
            "        cca2: \"cd\",\n" +
            "        \"calling-code\": \"243\"\n" +
            "    }, {\n" +
            "        name: \"Congo (Republic)\",\n" +
            "        cca2: \"cg\",\n" +
            "        \"calling-code\": \"242\"\n" +
            "    }, {\n" +
            "        name: \"Cook Islands\",\n" +
            "        cca2: \"ck\",\n" +
            "        \"calling-code\": \"682\"\n" +
            "    }, {\n" +
            "        name: \"Costa Rica\",\n" +
            "        cca2: \"cr\",\n" +
            "        \"calling-code\": \"506\"\n" +
            "    }, {\n" +
            "        name: \"Côte d'Ivoire\",\n" +
            "        cca2: \"ci\",\n" +
            "        \"calling-code\": \"225\"\n" +
            "    }, {\n" +
            "        name: \"Croatia\",\n" +
            "        cca2: \"hr\",\n" +
            "        \"calling-code\": \"385\"\n" +
            "    }, {\n" +
            "        name: \"Cuba\",\n" +
            "        cca2: \"cu\",\n" +
            "        \"calling-code\": \"53\"\n" +
            "    }, {\n" +
            "        name: \"Cyprus\",\n" +
            "        cca2: \"cy\",\n" +
            "        \"calling-code\": \"357\"\n" +
            "    }, {\n" +
            "        name: \"Czech Republic\",\n" +
            "        cca2: \"cz\",\n" +
            "        \"calling-code\": \"420\"\n" +
            "    }, {\n" +
            "        name: \"Denmark\",\n" +
            "        cca2: \"dk\",\n" +
            "        \"calling-code\": \"45\"\n" +
            "    }, {\n" +
            "        name: \"Djibouti\",\n" +
            "        cca2: \"dj\",\n" +
            "        \"calling-code\": \"253\"\n" +
            "    }, {\n" +
            "        name: \"Dominica\",\n" +
            "        cca2: \"dm\",\n" +
            "        \"calling-code\": \"1767\"\n" +
            "    }, {\n" +
            "        name: \"Dominican Republic\",\n" +
            "        cca2: \"do\",\n" +
            "        \"calling-code\": \"1809\"\n" +
            "    }, {\n" +
            "        name: \"Ecuador\",\n" +
            "        cca2: \"ec\",\n" +
            "        \"calling-code\": \"593\"\n" +
            "    }, {\n" +
            "        name: \"Egypt\",\n" +
            "        cca2: \"eg\",\n" +
            "        \"calling-code\": \"20\"\n" +
            "    }, {\n" +
            "        name: \"El Salvador\",\n" +
            "        cca2: \"sv\",\n" +
            "        \"calling-code\": \"503\"\n" +
            "    }, {\n" +
            "        name: \"Equatorial Guinea\",\n" +
            "        cca2: \"gq\",\n" +
            "        \"calling-code\": \"240\"\n" +
            "    }, {\n" +
            "        name: \"Eritrea\",\n" +
            "        cca2: \"er\",\n" +
            "        \"calling-code\": \"291\"\n" +
            "    }, {\n" +
            "        name: \"Estonia\",\n" +
            "        cca2: \"ee\",\n" +
            "        \"calling-code\": \"372\"\n" +
            "    }, {\n" +
            "        name: \"Ethiopia\",\n" +
            "        cca2: \"et\",\n" +
            "        \"calling-code\": \"251\"\n" +
            "    }, {\n" +
            "        name: \"Faroe Islands\",\n" +
            "        cca2: \"fo\",\n" +
            "        \"calling-code\": \"298\"\n" +
            "    }, {\n" +
            "        name: \"Fiji\",\n" +
            "        cca2: \"fj\",\n" +
            "        \"calling-code\": \"679\"\n" +
            "    }, {\n" +
            "        name: \"Finland\",\n" +
            "        cca2: \"fi\",\n" +
            "        \"calling-code\": \"358\"\n" +
            "    }, {\n" +
            "        name: \"France\",\n" +
            "        cca2: \"fr\",\n" +
            "        \"calling-code\": \"33\"\n" +
            "    }, {\n" +
            "        name: \"French Polynesia\",\n" +
            "        cca2: \"pf\",\n" +
            "        \"calling-code\": \"689\"\n" +
            "    }, {\n" +
            "        name: \"Gabon\",\n" +
            "        cca2: \"ga\",\n" +
            "        \"calling-code\": \"241\"\n" +
            "    }, {\n" +
            "        name: \"Gambia\",\n" +
            "        cca2: \"gm\",\n" +
            "        \"calling-code\": \"220\"\n" +
            "    }, {\n" +
            "        name: \"Georgia\",\n" +
            "        cca2: \"ge\",\n" +
            "        \"calling-code\": \"995\"\n" +
            "    }, {\n" +
            "        name: \"Germany\",\n" +
            "        cca2: \"de\",\n" +
            "        \"calling-code\": \"49\"\n" +
            "    }, {\n" +
            "        name: \"Ghana\",\n" +
            "        cca2: \"gh\",\n" +
            "        \"calling-code\": \"233\"\n" +
            "    }, {\n" +
            "        name: \"Gibraltar\",\n" +
            "        cca2: \"gi\",\n" +
            "        \"calling-code\": \"350\"\n" +
            "    }, {\n" +
            "        name: \"Greece\",\n" +
            "        cca2: \"gr\",\n" +
            "        \"calling-code\": \"30\"\n" +
            "    }, {\n" +
            "        name: \"Greenland\",\n" +
            "        cca2: \"gl\",\n" +
            "        \"calling-code\": \"299\"\n" +
            "    }, {\n" +
            "        name: \"Grenada\",\n" +
            "        cca2: \"gd\",\n" +
            "        \"calling-code\": \"1473\"\n" +
            "    }, {\n" +
            "        name: \"Guadeloupe\",\n" +
            "        cca2: \"gp\",\n" +
            "        \"calling-code\": \"590\"\n" +
            "    }, {\n" +
            "        name: \"Guam\",\n" +
            "        cca2: \"gu\",\n" +
            "        \"calling-code\": \"1671\"\n" +
            "    }, {\n" +
            "        name: \"Guatemala\",\n" +
            "        cca2: \"gt\",\n" +
            "        \"calling-code\": \"502\"\n" +
            "    }, {\n" +
            "        name: \"Guernsey\",\n" +
            "        cca2: \"gg\",\n" +
            "        \"calling-code\": \"44\"\n" +
            "    }, {\n" +
            "        name: \"Guinea\",\n" +
            "        cca2: \"gn\",\n" +
            "        \"calling-code\": \"224\"\n" +
            "    }, {\n" +
            "        name: \"Guinea-Bissau\",\n" +
            "        cca2: \"gw\",\n" +
            "        \"calling-code\": \"245\"\n" +
            "    }, {\n" +
            "        name: \"Guyana\",\n" +
            "        cca2: \"gy\",\n" +
            "        \"calling-code\": \"592\"\n" +
            "    }, {\n" +
            "        name: \"Haiti\",\n" +
            "        cca2: \"ht\",\n" +
            "        \"calling-code\": \"509\"\n" +
            "    }, {\n" +
            "        name: \"Honduras\",\n" +
            "        cca2: \"hn\",\n" +
            "        \"calling-code\": \"504\"\n" +
            "    }, {\n" +
            "        name: \"Hong Kong\",\n" +
            "        cca2: \"hk\",\n" +
            "        \"calling-code\": \"852\"\n" +
            "    }, {\n" +
            "        name: \"Hungary\",\n" +
            "        cca2: \"hu\",\n" +
            "        \"calling-code\": \"36\"\n" +
            "    }, {\n" +
            "        name: \"Iceland\",\n" +
            "        cca2: \"is\",\n" +
            "        \"calling-code\": \"354\"\n" +
            "    }, {\n" +
            "        name: \"India\",\n" +
            "        cca2: \"in\",\n" +
            "        \"calling-code\": \"91\"\n" +
            "    }, {\n" +
            "        name: \"Indonesia\",\n" +
            "        cca2: \"id\",\n" +
            "        \"calling-code\": \"62\"\n" +
            "    }, {\n" +
            "        name: \"Iran\",\n" +
            "        cca2: \"ir\",\n" +
            "        \"calling-code\": \"98\"\n" +
            "    }, {\n" +
            "        name: \"Iraq\",\n" +
            "        cca2: \"iq\",\n" +
            "        \"calling-code\": \"964\"\n" +
            "    }, {\n" +
            "        name: \"Ireland\",\n" +
            "        cca2: \"ie\",\n" +
            "        \"calling-code\": \"353\"\n" +
            "    }, {\n" +
            "        name: \"Isle of Man\",\n" +
            "        cca2: \"im\",\n" +
            "        \"calling-code\": \"44\"\n" +
            "    }, {\n" +
            "        name: \"Israel\",\n" +
            "        cca2: \"il\",\n" +
            "        \"calling-code\": \"972\"\n" +
            "    }, {\n" +
            "        name: \"Italy\",\n" +
            "        cca2: \"it\",\n" +
            "        \"calling-code\": \"39\"\n" +
            "    }, {\n" +
            "        name: \"Jamaica\",\n" +
            "        cca2: \"jm\",\n" +
            "        \"calling-code\": \"1876\"\n" +
            "    }, {\n" +
            "        name: \"Japan\",\n" +
            "        cca2: \"jp\",\n" +
            "        \"calling-code\": \"81\"\n" +
            "    }, {\n" +
            "        name: \"Jersey\",\n" +
            "        cca2: \"je\",\n" +
            "        \"calling-code\": \"44\"\n" +
            "    }, {\n" +
            "        name: \"Jordan\",\n" +
            "        cca2: \"jo\",\n" +
            "        \"calling-code\": \"962\"\n" +
            "    }, {\n" +
            "        name: \"Kazakhstan\",\n" +
            "        cca2: \"kz\",\n" +
            "        \"calling-code\": \"7\"\n" +
            "    }, {\n" +
            "        name: \"Kenya\",\n" +
            "        cca2: \"ke\",\n" +
            "        \"calling-code\": \"254\"\n" +
            "    }, {\n" +
            "        name: \"Kiribati\",\n" +
            "        cca2: \"ki\",\n" +
            "        \"calling-code\": \"686\"\n" +
            "    }, {\n" +
            "        name: \"Kuwait\",\n" +
            "        cca2: \"kw\",\n" +
            "        \"calling-code\": \"965\"\n" +
            "    }, {\n" +
            "        name: \"Kyrgyzstan\",\n" +
            "        cca2: \"kg\",\n" +
            "        \"calling-code\": \"996\"\n" +
            "    }, {\n" +
            "        name: \"Laos\",\n" +
            "        cca2: \"la\",\n" +
            "        \"calling-code\": \"856\"\n" +
            "    }, {\n" +
            "        name: \"Latvia\",\n" +
            "        cca2: \"lv\",\n" +
            "        \"calling-code\": \"371\"\n" +
            "    }, {\n" +
            "        name: \"Lebanon\",\n" +
            "        cca2: \"lb\",\n" +
            "        \"calling-code\": \"961\"\n" +
            "    }, {\n" +
            "        name: \"Lesotho\",\n" +
            "        cca2: \"ls\",\n" +
            "        \"calling-code\": \"266\"\n" +
            "    }, {\n" +
            "        name: \"Liberia\",\n" +
            "        cca2: \"lr\",\n" +
            "        \"calling-code\": \"231\"\n" +
            "    }, {\n" +
            "        name: \"Libya\",\n" +
            "        cca2: \"ly\",\n" +
            "        \"calling-code\": \"218\"\n" +
            "    }, {\n" +
            "        name: \"Liechtenstein\",\n" +
            "        cca2: \"li\",\n" +
            "        \"calling-code\": \"423\"\n" +
            "    }, {\n" +
            "        name: \"Lithuania\",\n" +
            "        cca2: \"lt\",\n" +
            "        \"calling-code\": \"370\"\n" +
            "    }, {\n" +
            "        name: \"Luxembourg\",\n" +
            "        cca2: \"lu\",\n" +
            "        \"calling-code\": \"352\"\n" +
            "    }, {\n" +
            "        name: \"Macao\",\n" +
            "        cca2: \"mo\",\n" +
            "        \"calling-code\": \"853\"\n" +
            "    }, {\n" +
            "        name: \"Macedonia\",\n" +
            "        cca2: \"mk\",\n" +
            "        \"calling-code\": \"389\"\n" +
            "    }, {\n" +
            "        name: \"Madagascar\",\n" +
            "        cca2: \"mg\",\n" +
            "        \"calling-code\": \"261\"\n" +
            "    }, {\n" +
            "        name: \"Malawi\",\n" +
            "        cca2: \"mw\",\n" +
            "        \"calling-code\": \"265\"\n" +
            "    }, {\n" +
            "        name: \"Malaysia\",\n" +
            "        cca2: \"my\",\n" +
            "        \"calling-code\": \"60\"\n" +
            "    }, {\n" +
            "        name: \"Maldives\",\n" +
            "        cca2: \"mv\",\n" +
            "        \"calling-code\": \"960\"\n" +
            "    }, {\n" +
            "        name: \"Mali\",\n" +
            "        cca2: \"ml\",\n" +
            "        \"calling-code\": \"223\"\n" +
            "    }, {\n" +
            "        name: \"Malta\",\n" +
            "        cca2: \"mt\",\n" +
            "        \"calling-code\": \"356\"\n" +
            "    }, {\n" +
            "        name: \"Marshall Islands\",\n" +
            "        cca2: \"mh\",\n" +
            "        \"calling-code\": \"692\"\n" +
            "    }, {\n" +
            "        name: \"Martinique\",\n" +
            "        cca2: \"mq\",\n" +
            "        \"calling-code\": \"596\"\n" +
            "    }, {\n" +
            "        name: \"Mauritania\",\n" +
            "        cca2: \"mr\",\n" +
            "        \"calling-code\": \"222\"\n" +
            "    }, {\n" +
            "        name: \"Mauritius\",\n" +
            "        cca2: \"mu\",\n" +
            "        \"calling-code\": \"230\"\n" +
            "    }, {\n" +
            "        name: \"Mexico\",\n" +
            "        cca2: \"mx\",\n" +
            "        \"calling-code\": \"52\"\n" +
            "    }, {\n" +
            "        name: \"Micronesia\",\n" +
            "        cca2: \"fm\",\n" +
            "        \"calling-code\": \"691\"\n" +
            "    }, {\n" +
            "        name: \"Moldova\",\n" +
            "        cca2: \"md\",\n" +
            "        \"calling-code\": \"373\"\n" +
            "    }, {\n" +
            "        name: \"Monaco\",\n" +
            "        cca2: \"mc\",\n" +
            "        \"calling-code\": \"377\"\n" +
            "    }, {\n" +
            "        name: \"Mongolia\",\n" +
            "        cca2: \"mn\",\n" +
            "        \"calling-code\": \"976\"\n" +
            "    }, {\n" +
            "        name: \"Montenegro\",\n" +
            "        cca2: \"me\",\n" +
            "        \"calling-code\": \"382\"\n" +
            "    }, {\n" +
            "        name: \"Montserrat\",\n" +
            "        cca2: \"ms\",\n" +
            "        \"calling-code\": \"1664\"\n" +
            "    }, {\n" +
            "        name: \"Morocco\",\n" +
            "        cca2: \"ma\",\n" +
            "        \"calling-code\": \"212\"\n" +
            "    }, {\n" +
            "        name: \"Mozambique\",\n" +
            "        cca2: \"mz\",\n" +
            "        \"calling-code\": \"258\"\n" +
            "    }, {\n" +
            "        name: \"Myanmar (Burma)\",\n" +
            "        cca2: \"mm\",\n" +
            "        \"calling-code\": \"95\"\n" +
            "    }, {\n" +
            "        name: \"Namibia\",\n" +
            "        cca2: \"na\",\n" +
            "        \"calling-code\": \"264\"\n" +
            "    }, {\n" +
            "        name: \"Nauru\",\n" +
            "        cca2: \"nr\",\n" +
            "        \"calling-code\": \"674\"\n" +
            "    }, {\n" +
            "        name: \"Nepal\",\n" +
            "        cca2: \"np\",\n" +
            "        \"calling-code\": \"977\"\n" +
            "    }, {\n" +
            "        name: \"Netherlands\",\n" +
            "        cca2: \"nl\",\n" +
            "        \"calling-code\": \"31\"\n" +
            "    }, {\n" +
            "        name: \"New Caledonia\",\n" +
            "        cca2: \"nc\",\n" +
            "        \"calling-code\": \"687\"\n" +
            "    }, {\n" +
            "        name: \"New Zealand\",\n" +
            "        cca2: \"nz\",\n" +
            "        \"calling-code\": \"64\"\n" +
            "    }, {\n" +
            "        name: \"Nicaragua\",\n" +
            "        cca2: \"ni\",\n" +
            "        \"calling-code\": \"505\"\n" +
            "    }, {\n" +
            "        name: \"Niger\",\n" +
            "        cca2: \"ne\",\n" +
            "        \"calling-code\": \"227\"\n" +
            "    }, {\n" +
            "        name: \"Nigeria\",\n" +
            "        cca2: \"ng\",\n" +
            "        \"calling-code\": \"234\"\n" +
            "    }, {\n" +
            "        name: \"North Korea\",\n" +
            "        cca2: \"kp\",\n" +
            "        \"calling-code\": \"850\"\n" +
            "    }, {\n" +
            "        name: \"Norway\",\n" +
            "        cca2: \"no\",\n" +
            "        \"calling-code\": \"47\"\n" +
            "    }, {\n" +
            "        name: \"Oman\",\n" +
            "        cca2: \"om\",\n" +
            "        \"calling-code\": \"968\"\n" +
            "    }, {\n" +
            "        name: \"Pakistan\",\n" +
            "        cca2: \"pk\",\n" +
            "        \"calling-code\": \"92\"\n" +
            "    }, {\n" +
            "        name: \"Palau\",\n" +
            "        cca2: \"pw\",\n" +
            "        \"calling-code\": \"680\"\n" +
            "    }, {\n" +
            "        name: \"Palestinian Territory\",\n" +
            "        cca2: \"ps\",\n" +
            "        \"calling-code\": \"970\"\n" +
            "    }, {\n" +
            "        name: \"Panama\",\n" +
            "        cca2: \"pa\",\n" +
            "        \"calling-code\": \"507\"\n" +
            "    }, {\n" +
            "        name: \"Papua New Guinea\",\n" +
            "        cca2: \"pg\",\n" +
            "        \"calling-code\": \"675\"\n" +
            "    }, {\n" +
            "        name: \"Paraguay\",\n" +
            "        cca2: \"py\",\n" +
            "        \"calling-code\": \"595\"\n" +
            "    }, {\n" +
            "        name: \"Peru\",\n" +
            "        cca2: \"pe\",\n" +
            "        \"calling-code\": \"51\"\n" +
            "    }, {\n" +
            "        name: \"Philippines\",\n" +
            "        cca2: \"ph\",\n" +
            "        \"calling-code\": \"63\"\n" +
            "    }, {\n" +
            "        name: \"Poland\",\n" +
            "        cca2: \"pl\",\n" +
            "        \"calling-code\": \"48\"\n" +
            "    }, {\n" +
            "        name: \"Portugal\",\n" +
            "        cca2: \"pt\",\n" +
            "        \"calling-code\": \"351\"\n" +
            "    }, {\n" +
            "        name: \"Puerto Rico\",\n" +
            "        cca2: \"pr\",\n" +
            "        \"calling-code\": \"1787\"\n" +
            "    }, {\n" +
            "        name: \"Qatar\",\n" +
            "        cca2: \"qa\",\n" +
            "        \"calling-code\": \"974\"\n" +
            "    }, {\n" +
            "        name: \"Réunion\",\n" +
            "        cca2: \"re\",\n" +
            "        \"calling-code\": \"262\"\n" +
            "    }, {\n" +
            "        name: \"Romania\",\n" +
            "        cca2: \"ro\",\n" +
            "        \"calling-code\": \"40\"\n" +
            "    }, {\n" +
            "        name: \"Russian Federation\",\n" +
            "        cca2: \"ru\",\n" +
            "        \"calling-code\": \"7\"\n" +
            "    }, {\n" +
            "        name: \"Rwanda\",\n" +
            "        cca2: \"rw\",\n" +
            "        \"calling-code\": \"250\"\n" +
            "    }, {\n" +
            "        name: \"Saint Kitts and Nevis\",\n" +
            "        cca2: \"kn\",\n" +
            "        \"calling-code\": \"1869\"\n" +
            "    }, {\n" +
            "        name: \"Saint Lucia\",\n" +
            "        cca2: \"lc\",\n" +
            "        \"calling-code\": \"1758\"\n" +
            "    }, {\n" +
            "        name: \"Saint Vincent and the Grenadines\",\n" +
            "        cca2: \"vc\",\n" +
            "        \"calling-code\": \"1784\"\n" +
            "    }, {\n" +
            "        name: \"Samoa\",\n" +
            "        cca2: \"ws\",\n" +
            "        \"calling-code\": \"685\"\n" +
            "    }, {\n" +
            "        name: \"San Marino\",\n" +
            "        cca2: \"sm\",\n" +
            "        \"calling-code\": \"378\"\n" +
            "    }, {\n" +
            "        name: \"São Tomé and Príncipe\",\n" +
            "        cca2: \"st\",\n" +
            "        \"calling-code\": \"239\"\n" +
            "    }, {\n" +
            "        name: \"Saudi Arabia\",\n" +
            "        cca2: \"sa\",\n" +
            "        \"calling-code\": \"966\"\n" +
            "    }, {\n" +
            "        name: \"Senegal\",\n" +
            "        cca2: \"sn\",\n" +
            "        \"calling-code\": \"221\"\n" +
            "    }, {\n" +
            "        name: \"Serbia\",\n" +
            "        cca2: \"rs\",\n" +
            "        \"calling-code\": \"381\"\n" +
            "    }, {\n" +
            "        name: \"Seychelles\",\n" +
            "        cca2: \"sc\",\n" +
            "        \"calling-code\": \"248\"\n" +
            "    }, {\n" +
            "        name: \"Sierra Leone\",\n" +
            "        cca2: \"sl\",\n" +
            "        \"calling-code\": \"232\"\n" +
            "    }, {\n" +
            "        name: \"Singapore\",\n" +
            "        cca2: \"sg\",\n" +
            "        \"calling-code\": \"65\"\n" +
            "    }, {\n" +
            "        name: \"Slovakia\",\n" +
            "        cca2: \"sk\",\n" +
            "        \"calling-code\": \"421\"\n" +
            "    }, {\n" +
            "        name: \"Slovenia\",\n" +
            "        cca2: \"si\",\n" +
            "        \"calling-code\": \"386\"\n" +
            "    }, {\n" +
            "        name: \"Solomon Islands\",\n" +
            "        cca2: \"sb\",\n" +
            "        \"calling-code\": \"677\"\n" +
            "    }, {\n" +
            "        name: \"Somalia\",\n" +
            "        cca2: \"so\",\n" +
            "        \"calling-code\": \"252\"\n" +
            "    }, {\n" +
            "        name: \"South Africa\",\n" +
            "        cca2: \"za\",\n" +
            "        \"calling-code\": \"27\"\n" +
            "    }, {\n" +
            "        name: \"South Korea\",\n" +
            "        cca2: \"kr\",\n" +
            "        \"calling-code\": \"82\"\n" +
            "    }, {\n" +
            "        name: \"Spain\",\n" +
            "        cca2: \"es\",\n" +
            "        \"calling-code\": \"34\"\n" +
            "    }, {\n" +
            "        name: \"Sri Lanka\",\n" +
            "        cca2: \"lk\",\n" +
            "        \"calling-code\": \"94\"\n" +
            "    }, {\n" +
            "        name: \"Sudan\",\n" +
            "        cca2: \"sd\",\n" +
            "        \"calling-code\": \"249\"\n" +
            "    }, {\n" +
            "        name: \"Suriname\",\n" +
            "        cca2: \"sr\",\n" +
            "        \"calling-code\": \"597\"\n" +
            "    }, {\n" +
            "        name: \"Swaziland\",\n" +
            "        cca2: \"sz\",\n" +
            "        \"calling-code\": \"268\"\n" +
            "    }, {\n" +
            "        name: \"Sweden\",\n" +
            "        cca2: \"se\",\n" +
            "        \"calling-code\": \"46\"\n" +
            "    }, {\n" +
            "        name: \"Switzerland\",\n" +
            "        cca2: \"ch\",\n" +
            "        \"calling-code\": \"41\"\n" +
            "    }, {\n" +
            "        name: \"Syrian Arab Republic\",\n" +
            "        cca2: \"sy\",\n" +
            "        \"calling-code\": \"963\"\n" +
            "    }, {\n" +
            "        name: \"Taiwan, Province of China\",\n" +
            "        cca2: \"tw\",\n" +
            "        \"calling-code\": \"886\"\n" +
            "    }, {\n" +
            "        name: \"Tajikistan\",\n" +
            "        cca2: \"tj\",\n" +
            "        \"calling-code\": \"992\"\n" +
            "    }, {\n" +
            "        name: \"Tanzania\",\n" +
            "        cca2: \"tz\",\n" +
            "        \"calling-code\": \"255\"\n" +
            "    }, {\n" +
            "        name: \"Thailand\",\n" +
            "        cca2: \"th\",\n" +
            "        \"calling-code\": \"66\"\n" +
            "    }, {\n" +
            "        name: \"Timor-Leste\",\n" +
            "        cca2: \"tl\",\n" +
            "        \"calling-code\": \"670\"\n" +
            "    }, {\n" +
            "        name: \"Togo\",\n" +
            "        cca2: \"tg\",\n" +
            "        \"calling-code\": \"228\"\n" +
            "    }, {\n" +
            "        name: \"Tonga\",\n" +
            "        cca2: \"to\",\n" +
            "        \"calling-code\": \"676\"\n" +
            "    }, {\n" +
            "        name: \"Trinidad and Tobago\",\n" +
            "        cca2: \"tt\",\n" +
            "        \"calling-code\": \"1868\"\n" +
            "    }, {\n" +
            "        name: \"Tunisia\",\n" +
            "        cca2: \"tn\",\n" +
            "        \"calling-code\": \"216\"\n" +
            "    }, {\n" +
            "        name: \"Turkey\",\n" +
            "        cca2: \"tr\",\n" +
            "        \"calling-code\": \"90\"\n" +
            "    }, {\n" +
            "        name: \"Turkmenistan\",\n" +
            "        cca2: \"tm\",\n" +
            "        \"calling-code\": \"993\"\n" +
            "    }, {\n" +
            "        name: \"Turks and Caicos Islands\",\n" +
            "        cca2: \"tc\",\n" +
            "        \"calling-code\": \"1649\"\n" +
            "    }, {\n" +
            "        name: \"Tuvalu\",\n" +
            "        cca2: \"tv\",\n" +
            "        \"calling-code\": \"688\"\n" +
            "    }, {\n" +
            "        name: \"Uganda\",\n" +
            "        cca2: \"ug\",\n" +
            "        \"calling-code\": \"256\"\n" +
            "    }, {\n" +
            "        name: \"Ukraine\",\n" +
            "        cca2: \"ua\",\n" +
            "        \"calling-code\": \"380\"\n" +
            "    }, {\n" +
            "        name: \"United Arab Emirates\",\n" +
            "        cca2: \"ae\",\n" +
            "        \"calling-code\": \"971\"\n" +
            "    }, {\n" +
            "        name: \"United Kingdom\",\n" +
            "        cca2: \"gb\",\n" +
            "        \"calling-code\": \"44\"\n" +
            "    }, {\n" +
            "        name: \"United States\",\n" +
            "        cca2: \"us\",\n" +
            "        \"calling-code\": \"1\"\n" +
            "    }, {\n" +
            "        name: \"Uruguay\",\n" +
            "        cca2: \"uy\",\n" +
            "        \"calling-code\": \"598\"\n" +
            "    }, {\n" +
            "        name: \"Uzbekistan\",\n" +
            "        cca2: \"uz\",\n" +
            "        \"calling-code\": \"998\"\n" +
            "    }, {\n" +
            "        name: \"Vanuatu\",\n" +
            "        cca2: \"vu\",\n" +
            "        \"calling-code\": \"678\"\n" +
            "    }, {\n" +
            "        name: \"Vatican City\",\n" +
            "        cca2: \"va\",\n" +
            "        \"calling-code\": \"379\"\n" +
            "    }, {\n" +
            "        name: \"Venezuela\",\n" +
            "        cca2: \"ve\",\n" +
            "        \"calling-code\": \"58\"\n" +
            "    }, {\n" +
            "        name: \"Viet Nam\",\n" +
            "        cca2: \"vn\",\n" +
            "        \"calling-code\": \"84\"\n" +
            "    }, {\n" +
            "        name: \"Virgin Islands (British)\",\n" +
            "        cca2: \"vg\",\n" +
            "        \"calling-code\": \"1284\"\n" +
            "    }, {\n" +
            "        name: \"Virgin Islands (U.S.)\",\n" +
            "        cca2: \"vi\",\n" +
            "        \"calling-code\": \"1340\"\n" +
            "    }, {\n" +
            "        name: \"Western Sahara\",\n" +
            "        cca2: \"eh\",\n" +
            "        \"calling-code\": \"212\"\n" +
            "    }, {\n" +
            "        name: \"Yemen\",\n" +
            "        cca2: \"ye\",\n" +
            "        \"calling-code\": \"967\"\n" +
            "    }, {\n" +
            "        name: \"Zambia\",\n" +
            "        cca2: \"zm\",\n" +
            "        \"calling-code\": \"260\"\n" +
            "    }, {\n" +
            "        name: \"Zimbabwe\",\n" +
            "        cca2: \"zw\",\n" +
            "        \"calling-code\": \"263\"\n" +
            "    } ]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        customer_driver_login_id = sharedpreferences.getLong("customer_driver_id", 0);
        Login_Type = sharedpreferences.getString("login_as", "");
        rl_login_register = (RelativeLayout) findViewById(R.id.rl_login_register);
        rl_login = (RelativeLayout) findViewById(R.id.rl_login);
        rl_register = (RelativeLayout) findViewById(R.id.rl_register);
        rl_login.setOnClickListener(this);
        rl_register.setOnClickListener(this);
        if (customer_driver_login_id != 0) {
            if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
//                Toast.makeText(SplashPage.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(SplashPage.this).create();
                //				alertDialog.setTitle("Your phone does not support GPS or NETWORK access, you can still use the application but some features may not work properly.");
                alertDialog.setMessage("Please check your Internet connection.");//but some features may not work properly
                //alertDialog.setIcon(R.drawable.ic_launcher);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                                            SplashPage.this.finish();
                    }
                });
                alertDialog.show();
            } else {
                rl_login_register.setVisibility(View.GONE);
                if (Login_Type.equalsIgnoreCase("driver")) {
                    Intent in = new Intent(SplashPage.this, DriverNavigation.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    startService();
                    this.finish();
                } else {
                    Intent in = new Intent(SplashPage.this, PassengerNavigation.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    stopService();
                    this.finish();
                }
            }
        }
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ConnectionDetector.getInstance().setGcmToken(refreshedToken);
        Androi_Id = Secure.getString(SplashPage.this.getContentResolver(),
                Secure.ANDROID_ID);
        if (TextUtils.isEmpty(refreshedToken)) {
            Intent in = new Intent(SplashPage.this, SplashPage.class);
            startActivity(in);
            this.finish();
        }
//        if(refreshedToken != null && !refreshedToken .isEmpty())
//        {
//
//        }

        Hash_file_maps = new HashMap<String, Integer>();

        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        indicator = (PagerIndicator) findViewById(R.id.custom_indicator);

        Hash_file_maps.put("Android CupCake", R.drawable.bikeslider);
        Hash_file_maps.put("Android Donut", R.drawable.bikesliderr);
        Hash_file_maps.put("Android Eclair", R.drawable.taxislider);

        for (String name : Hash_file_maps.keySet()) {

            DefaultSliderView textSliderView = new DefaultSliderView(SplashPage.this);
            textSliderView
//                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.setBackgroundColor(Color.TRANSPARENT);
            sliderLayout.addSlider(textSliderView);
        }
//        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.setCustomIndicator(indicator);
        sliderLayout.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_register:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                Toast.makeText(SplashPage.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(SplashPage.this, RegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_login:
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
                 Toast.makeText(SplashPage.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent in = new Intent(SplashPage.this, LoginActivity.class);
                    startActivity(in);
                }
                break;
        }
    }

    public void startService() {
        if (ConnectionDetector.getInstance().isConnectingToInternet()) {
            startService(new Intent(getBaseContext(), MyService.class));
        }
    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

//        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderLayout.startAutoCycle();
    }
}
