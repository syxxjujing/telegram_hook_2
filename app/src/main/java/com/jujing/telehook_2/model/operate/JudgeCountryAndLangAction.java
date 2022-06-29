package com.jujing.telehook_2.model.operate;

import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JudgeCountryAndLangAction {

    private static final String TAG = "JudgeCountryAndLangAction";

    public static void initCountry(){
        try {
            String str = "[\n" +
                    "  {\n" +
                    "    \"area_code\": \"0093\",\n" +
                    "    \"country_id\": 2,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"阿富汗\",\n" +
                    "    \"name_en\": \"Afghanistan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00355\",\n" +
                    "    \"country_id\": 3,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"阿尔巴尼亚\",\n" +
                    "    \"name_en\": \"Albania\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00213\",\n" +
                    "    \"country_id\": 4,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"阿尔及利亚\",\n" +
                    "    \"name_en\": \"Algeria\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00376\",\n" +
                    "    \"country_id\": 5,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"安道尔共和国\",\n" +
                    "    \"name_en\": \"Andorra\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00244\",\n" +
                    "    \"country_id\": 1,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"安哥拉\",\n" +
                    "    \"name_en\": \"Angola\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001264\",\n" +
                    "    \"country_id\": 6,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"安圭拉岛\",\n" +
                    "    \"name_en\": \"Anguilla\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001268\",\n" +
                    "    \"country_id\": 7,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"安提瓜和巴布达\",\n" +
                    "    \"name_en\": \"Antigua and Barbuda\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0054\",\n" +
                    "    \"country_id\": 8,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"阿根廷\",\n" +
                    "    \"name_en\": \"Argentina\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00374\",\n" +
                    "    \"country_id\": 9,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"亚美尼亚\",\n" +
                    "    \"name_en\": \"Armenia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00297\",\n" +
                    "    \"country_id\": 194,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"阿鲁巴\",\n" +
                    "    \"name_en\": \"Aruba\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00247\",\n" +
                    "    \"country_id\": 10,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"阿森松\",\n" +
                    "    \"name_en\": \"Ascension\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0061\",\n" +
                    "    \"country_id\": 11,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"澳大利亚\",\n" +
                    "    \"name_en\": \"Australia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00672\",\n" +
                    "    \"country_id\": 195,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"澳大利亚海外领地\",\n" +
                    "    \"name_en\": \"Australian overseas territories\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0043\",\n" +
                    "    \"country_id\": 12,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"奥地利\",\n" +
                    "    \"name_en\": \"Austria\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00994\",\n" +
                    "    \"country_id\": 13,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"阿塞拜疆\",\n" +
                    "    \"name_en\": \"Azerbaijan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001242\",\n" +
                    "    \"country_id\": 14,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"巴哈马\",\n" +
                    "    \"name_en\": \"Bahamas\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00973\",\n" +
                    "    \"country_id\": 15,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"巴林\",\n" +
                    "    \"name_en\": \"Bahrain\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00880\",\n" +
                    "    \"country_id\": 16,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"孟加拉国\",\n" +
                    "    \"name_en\": \"Bangladesh\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001246\",\n" +
                    "    \"country_id\": 17,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"巴巴多斯\",\n" +
                    "    \"name_en\": \"Barbados\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00375\",\n" +
                    "    \"country_id\": 18,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"白俄罗斯\",\n" +
                    "    \"name_en\": \"Belarus\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0032\",\n" +
                    "    \"country_id\": 19,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"比利时\",\n" +
                    "    \"name_en\": \"Belgium\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00501\",\n" +
                    "    \"country_id\": 20,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"伯利兹\",\n" +
                    "    \"name_en\": \"Belize\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00229\",\n" +
                    "    \"country_id\": 21,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"贝宁\",\n" +
                    "    \"name_en\": \"Benin\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001441\",\n" +
                    "    \"country_id\": 22,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"百慕大群岛\",\n" +
                    "    \"name_en\": \"Bermuda\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00975\",\n" +
                    "    \"country_id\": 196,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"不丹\",\n" +
                    "    \"name_en\": \"Bhutan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00591\",\n" +
                    "    \"country_id\": 23,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"玻利维亚\",\n" +
                    "    \"name_en\": \"Bolivia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00387\",\n" +
                    "    \"country_id\": 197,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"波斯尼亚和黑塞哥维那\",\n" +
                    "    \"name_en\": \"Bosnia and Herzegovina\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00267\",\n" +
                    "    \"country_id\": 24,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"博茨瓦纳\",\n" +
                    "    \"name_en\": \"Botswana\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0055\",\n" +
                    "    \"country_id\": 25,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"巴西\",\n" +
                    "    \"name_en\": \"Brazil\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001284\",\n" +
                    "    \"country_id\": 229,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"英属维尔京群岛\",\n" +
                    "    \"name_en\": \"British Virgin Islands\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00673\",\n" +
                    "    \"country_id\": 26,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"文莱\",\n" +
                    "    \"name_en\": \"Brunei\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00359\",\n" +
                    "    \"country_id\": 27,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"保加利亚\",\n" +
                    "    \"name_en\": \"Bulgaria\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00226\",\n" +
                    "    \"country_id\": 28,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"布基纳法索\",\n" +
                    "    \"name_en\": \"Burkina Faso\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00257\",\n" +
                    "    \"country_id\": 30,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"布隆迪\",\n" +
                    "    \"name_en\": \"Burundi\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00855\",\n" +
                    "    \"country_id\": 85,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"柬埔寨\",\n" +
                    "    \"name_en\": \"Cambodia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00237\",\n" +
                    "    \"country_id\": 31,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"喀麦隆\",\n" +
                    "    \"name_en\": \"Cameroon\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001\",\n" +
                    "    \"country_id\": 32,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"加拿大\",\n" +
                    "    \"name_en\": \"Canada\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00238\",\n" +
                    "    \"country_id\": 198,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"佛得角\",\n" +
                    "    \"name_en\": \"Cape Verde\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001345\",\n" +
                    "    \"country_id\": 33,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"开曼群岛\",\n" +
                    "    \"name_en\": \"Cayman Islands\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00236\",\n" +
                    "    \"country_id\": 34,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"中非共和国\",\n" +
                    "    \"name_en\": \"Central African Republic\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00235\",\n" +
                    "    \"country_id\": 35,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"乍得\",\n" +
                    "    \"name_en\": \"Chad\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0056\",\n" +
                    "    \"country_id\": 36,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"智利\",\n" +
                    "    \"name_en\": \"Chile\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0086\",\n" +
                    "    \"country_id\": 37,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"中国大陆\",\n" +
                    "    \"name_en\": \"China\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0057\",\n" +
                    "    \"country_id\": 38,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"哥伦比亚\",\n" +
                    "    \"name_en\": \"Colombia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00269\",\n" +
                    "    \"country_id\": 199,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"科摩罗群岛\",\n" +
                    "    \"name_en\": \"Comoros Islands\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00243\",\n" +
                    "    \"country_id\": 39,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"刚果\",\n" +
                    "    \"name_en\": \"Congo\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00682\",\n" +
                    "    \"country_id\": 40,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"库克群岛\",\n" +
                    "    \"name_en\": \"Cook Islands.\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00506\",\n" +
                    "    \"country_id\": 41,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"哥斯达黎加\",\n" +
                    "    \"name_en\": \"Costa Rica\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00385\",\n" +
                    "    \"country_id\": 200,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"克罗地亚\",\n" +
                    "    \"name_en\": \"Croatia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0053\",\n" +
                    "    \"country_id\": 42,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"古巴\",\n" +
                    "    \"name_en\": \"Cuba\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00357\",\n" +
                    "    \"country_id\": 43,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"塞浦路斯\",\n" +
                    "    \"name_en\": \"Cyprus\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00420\",\n" +
                    "    \"country_id\": 44,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"捷克\",\n" +
                    "    \"name_en\": \"Czech Republic\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0045\",\n" +
                    "    \"country_id\": 45,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"丹麦\",\n" +
                    "    \"name_en\": \"Denmark\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00246\",\n" +
                    "    \"country_id\": 201,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"迭戈加西亚群岛\",\n" +
                    "    \"name_en\": \"Diego Garcia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00253\",\n" +
                    "    \"country_id\": 46,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"吉布提\",\n" +
                    "    \"name_en\": \"Djibouti\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001767\",\n" +
                    "    \"country_id\": 232,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"多米尼克国\",\n" +
                    "    \"name_en\": \"Dominica\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001809\",\n" +
                    "    \"country_id\": 47,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"多米尼加共和国\",\n" +
                    "    \"name_en\": \"Dominican Republic\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00670\",\n" +
                    "    \"country_id\": 202,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"东帝汶\",\n" +
                    "    \"name_en\": \"East Timor\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00593\",\n" +
                    "    \"country_id\": 48,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"厄瓜多尔\",\n" +
                    "    \"name_en\": \"Ecuador\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0020\",\n" +
                    "    \"country_id\": 49,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"埃及\",\n" +
                    "    \"name_en\": \"Egypt\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00503\",\n" +
                    "    \"country_id\": 50,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"萨尔瓦多\",\n" +
                    "    \"name_en\": \"El Salvador\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"009714\",\n" +
                    "    \"country_id\": 230,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"迪拜酋长国\",\n" +
                    "    \"name_en\": \"Emirate of Dubai\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00240\",\n" +
                    "    \"country_id\": 203,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"赤道几内亚\",\n" +
                    "    \"name_en\": \"Equatorial Guinea\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00291\",\n" +
                    "    \"country_id\": 204,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"厄立特里亚\",\n" +
                    "    \"name_en\": \"Eritrea\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00372\",\n" +
                    "    \"country_id\": 51,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"爱沙尼亚\",\n" +
                    "    \"name_en\": \"Estonia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00251\",\n" +
                    "    \"country_id\": 52,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"埃塞俄比亚\",\n" +
                    "    \"name_en\": \"Ethiopia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00500\",\n" +
                    "    \"country_id\": 205,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"福克兰群岛\",\n" +
                    "    \"name_en\": \"Falkland Islands\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00298\",\n" +
                    "    \"country_id\": 206,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"法罗群岛\",\n" +
                    "    \"name_en\": \"Faroe Islands\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00679\",\n" +
                    "    \"country_id\": 53,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"斐济\",\n" +
                    "    \"name_en\": \"Fiji\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00358\",\n" +
                    "    \"country_id\": 54,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"芬兰\",\n" +
                    "    \"name_en\": \"Finland\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0033\",\n" +
                    "    \"country_id\": 55,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"法国\",\n" +
                    "    \"name_en\": \"France\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00594\",\n" +
                    "    \"country_id\": 56,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"法属圭亚那\",\n" +
                    "    \"name_en\": \"French Guiana\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00689\",\n" +
                    "    \"country_id\": 136,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"法属玻利尼西亚\",\n" +
                    "    \"name_en\": \"French Polynesia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00241\",\n" +
                    "    \"country_id\": 57,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"加蓬\",\n" +
                    "    \"name_en\": \"Gabon\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00220\",\n" +
                    "    \"country_id\": 58,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"冈比亚\",\n" +
                    "    \"name_en\": \"Gambia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00995\",\n" +
                    "    \"country_id\": 59,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"格鲁吉亚\",\n" +
                    "    \"name_en\": \"Georgia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0049\",\n" +
                    "    \"country_id\": 60,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"德国\",\n" +
                    "    \"name_en\": \"Germany\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00233\",\n" +
                    "    \"country_id\": 61,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"加纳\",\n" +
                    "    \"name_en\": \"Ghana\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00350\",\n" +
                    "    \"country_id\": 62,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"直布罗陀\",\n" +
                    "    \"name_en\": \"Gibraltar\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0030\",\n" +
                    "    \"country_id\": 63,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"希腊\",\n" +
                    "    \"name_en\": \"Greece\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00299\",\n" +
                    "    \"country_id\": 207,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"格陵兰岛\",\n" +
                    "    \"name_en\": \"Greenland\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001473\",\n" +
                    "    \"country_id\": 64,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"格林纳达\",\n" +
                    "    \"name_en\": \"Grenada\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00590\",\n" +
                    "    \"country_id\": 208,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"瓜德罗普\",\n" +
                    "    \"name_en\": \"Guadeloupe\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001671\",\n" +
                    "    \"country_id\": 65,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"关岛\",\n" +
                    "    \"name_en\": \"Guam\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00502\",\n" +
                    "    \"country_id\": 66,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"危地马拉\",\n" +
                    "    \"name_en\": \"Guatemala\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00224\",\n" +
                    "    \"country_id\": 67,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"几内亚\",\n" +
                    "    \"name_en\": \"Guinea\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00245\",\n" +
                    "    \"country_id\": 209,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"几内亚比绍\",\n" +
                    "    \"name_en\": \"Guinea-Bissau\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00592\",\n" +
                    "    \"country_id\": 68,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"圭亚那\",\n" +
                    "    \"name_en\": \"Guyana\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00509\",\n" +
                    "    \"country_id\": 69,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"海地\",\n" +
                    "    \"name_en\": \"Haiti\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00504\",\n" +
                    "    \"country_id\": 70,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"洪都拉斯\",\n" +
                    "    \"name_en\": \"Honduras\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00852\",\n" +
                    "    \"country_id\": 71,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"中国香港\",\n" +
                    "    \"name_en\": \"Hong Kong (China)\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0036\",\n" +
                    "    \"country_id\": 72,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"匈牙利\",\n" +
                    "    \"name_en\": \"Hungary\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00354\",\n" +
                    "    \"country_id\": 73,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"冰岛\",\n" +
                    "    \"name_en\": \"Iceland\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0091\",\n" +
                    "    \"country_id\": 74,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"印度\",\n" +
                    "    \"name_en\": \"India\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0062\",\n" +
                    "    \"country_id\": 75,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"印度尼西亚\",\n" +
                    "    \"name_en\": \"Indonesia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0098\",\n" +
                    "    \"country_id\": 76,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"伊朗\",\n" +
                    "    \"name_en\": \"Iran\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00964\",\n" +
                    "    \"country_id\": 77,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"伊拉克\",\n" +
                    "    \"name_en\": \"Iraq\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00353\",\n" +
                    "    \"country_id\": 78,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"爱尔兰\",\n" +
                    "    \"name_en\": \"Ireland\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00441624\",\n" +
                    "    \"country_id\": 235,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马恩岛\",\n" +
                    "    \"name_en\": \"Isle of Man\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00972\",\n" +
                    "    \"country_id\": 79,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"以色列\",\n" +
                    "    \"name_en\": \"Israel\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0039\",\n" +
                    "    \"country_id\": 80,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"意大利\",\n" +
                    "    \"name_en\": \"Italy\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00225\",\n" +
                    "    \"country_id\": 81,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"科特迪瓦\",\n" +
                    "    \"name_en\": \"Ivory Coast\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001876\",\n" +
                    "    \"country_id\": 82,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"牙买加\",\n" +
                    "    \"name_en\": \"Jamaica\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0081\",\n" +
                    "    \"country_id\": 83,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"日本\",\n" +
                    "    \"name_en\": \"Japan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0044\",\n" +
                    "    \"country_id\": 231,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"泽西\",\n" +
                    "    \"name_en\": \"Jersey\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00962\",\n" +
                    "    \"country_id\": 84,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"约旦\",\n" +
                    "    \"name_en\": \"Jordan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"007\",\n" +
                    "    \"country_id\": 86,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"哈萨克斯坦\",\n" +
                    "    \"name_en\": \"Kazakhstan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00254\",\n" +
                    "    \"country_id\": 87,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"肯尼亚\",\n" +
                    "    \"name_en\": \"Kenya\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00686\",\n" +
                    "    \"country_id\": 210,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"基里巴斯\",\n" +
                    "    \"name_en\": \"Kiribati\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0082\",\n" +
                    "    \"country_id\": 88,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"韩国\",\n" +
                    "    \"name_en\": \"Korea\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00383\",\n" +
                    "    \"country_id\": 234,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"科索沃\",\n" +
                    "    \"name_en\": \"Kosovo\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00965\",\n" +
                    "    \"country_id\": 89,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"科威特\",\n" +
                    "    \"name_en\": \"Kuwait\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00996\",\n" +
                    "    \"country_id\": 90,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"吉尔吉斯坦\",\n" +
                    "    \"name_en\": \"Kyrgyzstan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00856\",\n" +
                    "    \"country_id\": 91,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"老挝\",\n" +
                    "    \"name_en\": \"Laos\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00371\",\n" +
                    "    \"country_id\": 92,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"拉脱维亚\",\n" +
                    "    \"name_en\": \"Latvia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00961\",\n" +
                    "    \"country_id\": 93,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"黎巴嫩\",\n" +
                    "    \"name_en\": \"Lebanon\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00266\",\n" +
                    "    \"country_id\": 94,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"莱索托\",\n" +
                    "    \"name_en\": \"Lesotho\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00231\",\n" +
                    "    \"country_id\": 95,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"利比里亚\",\n" +
                    "    \"name_en\": \"Liberia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00218\",\n" +
                    "    \"country_id\": 96,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"利比亚\",\n" +
                    "    \"name_en\": \"Libya\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00423\",\n" +
                    "    \"country_id\": 97,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"列支敦士登\",\n" +
                    "    \"name_en\": \"Liechtenstein\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00370\",\n" +
                    "    \"country_id\": 98,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"立陶宛\",\n" +
                    "    \"name_en\": \"Lithuania\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00352\",\n" +
                    "    \"country_id\": 99,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"卢森堡\",\n" +
                    "    \"name_en\": \"Luxembourg\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00389\",\n" +
                    "    \"country_id\": 211,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马其顿\",\n" +
                    "    \"name_en\": \"Macedonia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00261\",\n" +
                    "    \"country_id\": 101,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马达加斯加\",\n" +
                    "    \"name_en\": \"Madagascar\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00265\",\n" +
                    "    \"country_id\": 102,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马拉维\",\n" +
                    "    \"name_en\": \"Malawi\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0060\",\n" +
                    "    \"country_id\": 103,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马来西亚\",\n" +
                    "    \"name_en\": \"Malaysia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00960\",\n" +
                    "    \"country_id\": 104,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马尔代夫\",\n" +
                    "    \"name_en\": \"Maldives\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00223\",\n" +
                    "    \"country_id\": 105,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马里\",\n" +
                    "    \"name_en\": \"Mali\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00356\",\n" +
                    "    \"country_id\": 106,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马耳他\",\n" +
                    "    \"name_en\": \"Malta\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00223\",\n" +
                    "    \"country_id\": 107,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马里亚那群岛\",\n" +
                    "    \"name_en\": \"Mariana Islands\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00692\",\n" +
                    "    \"country_id\": 212,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马绍尔群岛\",\n" +
                    "    \"name_en\": \"Marshall Islands\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00596\",\n" +
                    "    \"country_id\": 108,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"马提尼克\",\n" +
                    "    \"name_en\": \"Martinique\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00222\",\n" +
                    "    \"country_id\": 213,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"毛里塔尼亚\",\n" +
                    "    \"name_en\": \"Mauritania\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00230\",\n" +
                    "    \"country_id\": 109,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"毛里求斯\",\n" +
                    "    \"name_en\": \"Mauritius\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0052\",\n" +
                    "    \"country_id\": 110,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"墨西哥\",\n" +
                    "    \"name_en\": \"Mexico\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00691\",\n" +
                    "    \"country_id\": 214,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"密克罗尼西亚\",\n" +
                    "    \"name_en\": \"Micronesia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00373\",\n" +
                    "    \"country_id\": 111,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"摩尔多瓦\",\n" +
                    "    \"name_en\": \"Moldova\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00377\",\n" +
                    "    \"country_id\": 112,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"摩纳哥\",\n" +
                    "    \"name_en\": \"Monaco\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00976\",\n" +
                    "    \"country_id\": 113,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"蒙古\",\n" +
                    "    \"name_en\": \"Mongolia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00382\",\n" +
                    "    \"country_id\": 215,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"黑山\",\n" +
                    "    \"name_en\": \"Montenegro\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001664\",\n" +
                    "    \"country_id\": 114,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"蒙特塞拉特岛\",\n" +
                    "    \"name_en\": \"Montserrat\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00212\",\n" +
                    "    \"country_id\": 115,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"摩洛哥\",\n" +
                    "    \"name_en\": \"Morocco\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00258\",\n" +
                    "    \"country_id\": 116,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"莫桑比克\",\n" +
                    "    \"name_en\": \"Mozambique\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0095\",\n" +
                    "    \"country_id\": 29,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"缅甸\",\n" +
                    "    \"name_en\": \"Myanmar\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00264\",\n" +
                    "    \"country_id\": 117,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"纳米比亚\",\n" +
                    "    \"name_en\": \"Namibia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00674\",\n" +
                    "    \"country_id\": 118,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"瑙鲁\",\n" +
                    "    \"name_en\": \"Nauru\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00977\",\n" +
                    "    \"country_id\": 119,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"尼泊尔\",\n" +
                    "    \"name_en\": \"Nepal\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00599\",\n" +
                    "    \"country_id\": 120,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"荷属安的列斯\",\n" +
                    "    \"name_en\": \"Netheriands Antilles\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0031\",\n" +
                    "    \"country_id\": 121,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"荷兰\",\n" +
                    "    \"name_en\": \"Netherlands\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00687\",\n" +
                    "    \"country_id\": 216,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"新喀里多尼亚\",\n" +
                    "    \"name_en\": \"New Caledonia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0064\",\n" +
                    "    \"country_id\": 122,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"新西兰\",\n" +
                    "    \"name_en\": \"New Zealand\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00505\",\n" +
                    "    \"country_id\": 123,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"尼加拉瓜\",\n" +
                    "    \"name_en\": \"Nicaragua\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00227\",\n" +
                    "    \"country_id\": 124,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"尼日尔\",\n" +
                    "    \"name_en\": \"Niger\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00234\",\n" +
                    "    \"country_id\": 125,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"尼日利亚\",\n" +
                    "    \"name_en\": \"Nigeria\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00683\",\n" +
                    "    \"country_id\": 217,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"纽埃岛\",\n" +
                    "    \"name_en\": \"Niue\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00850\",\n" +
                    "    \"country_id\": 126,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"朝鲜\",\n" +
                    "    \"name_en\": \"North Korea\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0047\",\n" +
                    "    \"country_id\": 127,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"挪威\",\n" +
                    "    \"name_en\": \"Norway\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00968\",\n" +
                    "    \"country_id\": 128,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"阿曼\",\n" +
                    "    \"name_en\": \"Oman\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0092\",\n" +
                    "    \"country_id\": 129,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"巴基斯坦\",\n" +
                    "    \"name_en\": \"Pakistan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00680\",\n" +
                    "    \"country_id\": 218,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"帕劳\",\n" +
                    "    \"name_en\": \"Palau\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00970\",\n" +
                    "    \"country_id\": 219,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"巴勒斯坦\",\n" +
                    "    \"name_en\": \"Palestine\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00507\",\n" +
                    "    \"country_id\": 130,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"巴拿马\",\n" +
                    "    \"name_en\": \"Panama\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00675\",\n" +
                    "    \"country_id\": 131,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"巴布亚新几内亚\",\n" +
                    "    \"name_en\": \"Papua New Guinea\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00595\",\n" +
                    "    \"country_id\": 132,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"巴拉圭\",\n" +
                    "    \"name_en\": \"Paraguay\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0051\",\n" +
                    "    \"country_id\": 133,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"秘鲁\",\n" +
                    "    \"name_en\": \"Peru\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0063\",\n" +
                    "    \"country_id\": 134,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"菲律宾\",\n" +
                    "    \"name_en\": \"Philippines\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0048\",\n" +
                    "    \"country_id\": 135,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"波兰\",\n" +
                    "    \"name_en\": \"Poland\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00351\",\n" +
                    "    \"country_id\": 137,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"葡萄牙\",\n" +
                    "    \"name_en\": \"Portugal\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001\",\n" +
                    "    \"country_id\": 138,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"波多黎各\",\n" +
                    "    \"name_en\": \"Puerto Rico\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00974\",\n" +
                    "    \"country_id\": 139,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"卡塔尔\",\n" +
                    "    \"name_en\": \"Qatar\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00262\",\n" +
                    "    \"country_id\": 140,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"留尼旺\",\n" +
                    "    \"name_en\": \"Reunion\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0040\",\n" +
                    "    \"country_id\": 141,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"罗马尼亚\",\n" +
                    "    \"name_en\": \"Romania\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"007\",\n" +
                    "    \"country_id\": 142,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"俄罗斯\",\n" +
                    "    \"name_en\": \"Russia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00250\",\n" +
                    "    \"country_id\": 220,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"卢旺达\",\n" +
                    "    \"name_en\": \"Rwanda\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001869\",\n" +
                    "    \"country_id\": 233,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"圣基茨和尼维斯\",\n" +
                    "    \"name_en\": \"Saint Kitts and Nevis\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00508\",\n" +
                    "    \"country_id\": 222,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"圣皮埃尔和密克隆群岛\",\n" +
                    "    \"name_en\": \"Saint Pierre and Miquelon\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001784\",\n" +
                    "    \"country_id\": 144,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"圣文森特岛\",\n" +
                    "    \"name_en\": \"Saint Vincent\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00684\",\n" +
                    "    \"country_id\": 145,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"东萨摩亚(美)\",\n" +
                    "    \"name_en\": \"Samoa Eastern\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00685\",\n" +
                    "    \"country_id\": 146,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"西萨摩亚\",\n" +
                    "    \"name_en\": \"Samoa Western\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00378\",\n" +
                    "    \"country_id\": 147,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"圣马力诺\",\n" +
                    "    \"name_en\": \"San Marino\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00239\",\n" +
                    "    \"country_id\": 148,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"圣多美和普林西比\",\n" +
                    "    \"name_en\": \"Sao Tome and Principe\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00966\",\n" +
                    "    \"country_id\": 149,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"沙特阿拉伯\",\n" +
                    "    \"name_en\": \"Saudi Arabia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00221\",\n" +
                    "    \"country_id\": 150,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"塞内加尔\",\n" +
                    "    \"name_en\": \"Senegal\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00381\",\n" +
                    "    \"country_id\": 223,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"塞尔维亚\",\n" +
                    "    \"name_en\": \"Serbia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00248\",\n" +
                    "    \"country_id\": 151,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"塞舌尔\",\n" +
                    "    \"name_en\": \"Seychelles\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00232\",\n" +
                    "    \"country_id\": 152,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"塞拉利昂\",\n" +
                    "    \"name_en\": \"Sierra Leone\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0065\",\n" +
                    "    \"country_id\": 153,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"新加坡\",\n" +
                    "    \"name_en\": \"Singapore\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00421\",\n" +
                    "    \"country_id\": 154,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"斯洛伐克\",\n" +
                    "    \"name_en\": \"Slovakia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00386\",\n" +
                    "    \"country_id\": 155,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"斯洛文尼亚\",\n" +
                    "    \"name_en\": \"Slovenia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00677\",\n" +
                    "    \"country_id\": 156,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"所罗门群岛\",\n" +
                    "    \"name_en\": \"Solomon Islands\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00252\",\n" +
                    "    \"country_id\": 157,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"索马里\",\n" +
                    "    \"name_en\": \"Somalia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0027\",\n" +
                    "    \"country_id\": 158,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"南非\",\n" +
                    "    \"name_en\": \"South Africa\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0034\",\n" +
                    "    \"country_id\": 159,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"西班牙\",\n" +
                    "    \"name_en\": \"Spain\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0094\",\n" +
                    "    \"country_id\": 160,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"斯里兰卡\",\n" +
                    "    \"name_en\": \"Sri Lanka\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00290\",\n" +
                    "    \"country_id\": 221,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"圣赫勒拿岛\",\n" +
                    "    \"name_en\": \"St.Helena\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001758\",\n" +
                    "    \"country_id\": 161,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"圣卢西亚\",\n" +
                    "    \"name_en\": \"St.Lucia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001784\",\n" +
                    "    \"country_id\": 162,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"圣文森特\",\n" +
                    "    \"name_en\": \"St.Vincent\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00249\",\n" +
                    "    \"country_id\": 163,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"苏丹\",\n" +
                    "    \"name_en\": \"Sudan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00597\",\n" +
                    "    \"country_id\": 164,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"苏里南\",\n" +
                    "    \"name_en\": \"Suriname\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00268\",\n" +
                    "    \"country_id\": 165,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"斯威士兰\",\n" +
                    "    \"name_en\": \"Swaziland\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0046\",\n" +
                    "    \"country_id\": 166,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"瑞典\",\n" +
                    "    \"name_en\": \"Sweden\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0041\",\n" +
                    "    \"country_id\": 167,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"瑞士\",\n" +
                    "    \"name_en\": \"Switzerland\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00963\",\n" +
                    "    \"country_id\": 168,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"叙利亚\",\n" +
                    "    \"name_en\": \"Syria\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00886\",\n" +
                    "    \"country_id\": 169,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"中国台湾\",\n" +
                    "    \"name_en\": \"Taiwan (China)\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00992\",\n" +
                    "    \"country_id\": 170,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"塔吉克斯坦\",\n" +
                    "    \"name_en\": \"Tajikistan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00255\",\n" +
                    "    \"country_id\": 171,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"坦桑尼亚\",\n" +
                    "    \"name_en\": \"Tanzania\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0066\",\n" +
                    "    \"country_id\": 172,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"泰国\",\n" +
                    "    \"name_en\": \"Thailand\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00228\",\n" +
                    "    \"country_id\": 173,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"多哥\",\n" +
                    "    \"name_en\": \"Togo\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00690\",\n" +
                    "    \"country_id\": 224,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"托克劳群岛\",\n" +
                    "    \"name_en\": \"Tokelau\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00676\",\n" +
                    "    \"country_id\": 174,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"汤加\",\n" +
                    "    \"name_en\": \"Tonga\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001868\",\n" +
                    "    \"country_id\": 175,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"特立尼达和多巴哥\",\n" +
                    "    \"name_en\": \"Trinidad and Tobago\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00216\",\n" +
                    "    \"country_id\": 176,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"突尼斯\",\n" +
                    "    \"name_en\": \"Tunisia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0090\",\n" +
                    "    \"country_id\": 177,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"土耳其\",\n" +
                    "    \"name_en\": \"Turkey\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00993\",\n" +
                    "    \"country_id\": 178,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"土库曼斯坦\",\n" +
                    "    \"name_en\": \"Turkmenistan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00688\",\n" +
                    "    \"country_id\": 225,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"图瓦卢\",\n" +
                    "    \"name_en\": \"Tuvalu\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00256\",\n" +
                    "    \"country_id\": 179,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"乌干达\",\n" +
                    "    \"name_en\": \"Uganda\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00380\",\n" +
                    "    \"country_id\": 180,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"乌克兰\",\n" +
                    "    \"name_en\": \"Ukraine\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00971\",\n" +
                    "    \"country_id\": 181,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"阿拉伯联合酋长国\",\n" +
                    "    \"name_en\": \"United Arab Emirates\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0044\",\n" +
                    "    \"country_id\": 182,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"英国\",\n" +
                    "    \"name_en\": \"United Kingdom\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"001\",\n" +
                    "    \"country_id\": 183,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"美国\",\n" +
                    "    \"name_en\": \"United States of America\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00598\",\n" +
                    "    \"country_id\": 184,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"乌拉圭\",\n" +
                    "    \"name_en\": \"Uruguay\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00998\",\n" +
                    "    \"country_id\": 185,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"乌兹别克斯坦\",\n" +
                    "    \"name_en\": \"Uzbekistan\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00678\",\n" +
                    "    \"country_id\": 226,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"瓦努阿图\",\n" +
                    "    \"name_en\": \"Vanuatu\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00379\",\n" +
                    "    \"country_id\": 227,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"梵蒂冈城\",\n" +
                    "    \"name_en\": \"Vatican City\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0058\",\n" +
                    "    \"country_id\": 186,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"委内瑞拉\",\n" +
                    "    \"name_en\": \"Venezuela\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"0084\",\n" +
                    "    \"country_id\": 187,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"越南\",\n" +
                    "    \"name_en\": \"Vietnam\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00681\",\n" +
                    "    \"country_id\": 228,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"瓦利斯和富图纳\",\n" +
                    "    \"name_en\": \"Wallis and Futuna\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00967\",\n" +
                    "    \"country_id\": 188,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"也门\",\n" +
                    "    \"name_en\": \"Yemen\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00338\",\n" +
                    "    \"country_id\": 189,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"南斯拉夫\",\n" +
                    "    \"name_en\": \"Yugoslavia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00243\",\n" +
                    "    \"country_id\": 192,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"扎伊尔\",\n" +
                    "    \"name_en\": \"Zaire\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00260\",\n" +
                    "    \"country_id\": 193,\n" +
                    "    \"forbid\": false,\n" +
                    "    \"name_cn\": \"赞比亚\",\n" +
                    "    \"name_en\": \"Zambia\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"area_code\": \"00263\",\n" +
                    "    \"country_id\": 191,\n" +
                    "    \"forbid\": true,\n" +
                    "    \"name_cn\": \"津巴布韦\",\n" +
                    "    \"name_en\": \"Zimbabwe\"\n" +
                    "  }\n" +
                    "]";

            JSONArray jsonArray = new JSONArray(str);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name_cn = jsonObject.getString("name_cn");
                LoggerUtil.logAll(TAG,"name_cn ---->"+name_cn);
                countryList.add(name_cn);
            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG , "ee 1648----->" + CrashHandler.getInstance().printCrash(e));
        }


    }

    public static List<String> countryList = new ArrayList<>();

    public static String judgeCountry(long from_id, String messageContent) {
        String country = "";
        try {
            country = WriteFileUtil.read(Global.COUNTRY_JUDGE + from_id);
            LoggerUtil.logI(TAG + from_id, "country 15----->" + country);
            if (!TextUtils.isEmpty(country)) {
                if (!country.equals("fail")) {
                    return country;
                }
            }




            String result = TranslateAction.post(from_id + "", messageContent, "en");//都翻译成英语
            LoggerUtil.logI(TAG + from_id, "result 20----->" + result + "---->" + messageContent);
            String[] s = result.split(" ");
            if (s.length < 3) {
                country = "fail";
            }
            for (int i = 0; i < s.length; i++) {
                String s1 = s[i].toLowerCase();
                if (s1.contains("china") || s1.contains("chinese") || s1.contains("beijing") || s1.contains("\uD83C\uDDE8\uD83C\uDDF3")) {//中国
                    country = "zh";
                    break;
                }
                if (s1.contains("america") || s1.contains("english")
                        || s1.contains("washionton")
                        || s1.contains("u.s") || s1.contains("usa")
                        || s1.contains("united sta") || s1.contains("\uD83C\uDDFA\uD83C\uDDF8")
                        || s1.contains("deer")) {//美国
                    country = "en";
                    break;
                }
                if (s1.contains("uk") || s1.contains("u.k") || s1.contains("kingdom") || s1.contains("london") || s1.contains("\uD83C\uDDEC\uD83C\uDDE7")) {
                    country = "en_uk";
                    break;
                }
                if (s1.contains("japan") || s1.contains("japanese") || s1.contains("tokyo") || s1.contains("\uD83C\uDDEF\uD83C\uDDF5")) {
                    country = "jp";
                    break;
                }
                if (s1.contains("spain") || s1.contains("spaniard") || s1.contains("spanish") || s1.contains("madrid") || s1.contains("\uD83C\uDDEA\uD83C\uDDF8")) {
                    country = "xiba";
                    break;
                }
                if (s1.contains("brazil") || s1.contains("\uD83C\uDDE7\uD83C\uDDF7")) {
                    country = "baxi";
                    break;
                }
                if (s1.contains("portugal") || s1.contains("portu") || s1.contains("portuguese") || s1.contains("lisbon") || s1.contains("\uD83C\uDDF5\uD83C\uDDF9")) {
                    country = "puto";
                    break;
                }
                if (s1.contains("denmark") || s1.contains("danish") || s1.contains("copenhagen") || s1.contains("\uD83C\uDDE9\uD83C\uDDF0")) {
                    country = "danm";
                    break;
                }
                if (s1.contains("netherlands") || s1.contains("dutch") || s1.contains("amsterdam") || s1.contains("\uD83C\uDDF3\uD83C\uDDF1")) {
                    country = "helan";
                    break;
                }
                if (s1.contains("australia") || s1.contains("australian") || s1.contains("canberra") || s1.contains("\uD83C\uDDE6\uD83C\uDDFA")) {
                    country = "aod";
                    break;
                }
                if (s1.contains("thailand") || s1.contains("thailands") || s1.contains("thai") || s1.contains("bangkok") || s1.contains("\uD83C\uDDF9\uD83C\uDDED")) {
                    country = "tailan";
                    break;
                }
                if (s1.contains("zealand") || s1.contains("zealanders") || s1.contains("wellington") || s1.contains("\uD83C\uDDF3\uD83C\uDDFF")) {
                    country = "newel";
                    break;
                }
                if (s1.contains("india") || s1.contains("indians") || s1.contains("hindi") || s1.contains("delhi") || s1.contains("\uD83C\uDDEE\uD83C\uDDF3")) {
                    country = "yind";
                    break;
                }
                if (s1.contains("indonesia") || s1.contains("indones") || s1.contains("indonesian") || s1.contains("jakarta") || s1.contains("\uD83C\uDDEE\uD83C\uDDE9")) {
                    country = "yini";
                    break;
                }
                if (s1.contains("philippines") || s1.contains("filipino") || s1.contains("manila") || s1.contains("philippin") || s1.contains("\uD83C\uDDF5\uD83C\uDDED")) {
                    country = "pipli";
                    break;
                }

                if (s1.contains("iraq") || s1.contains("\uD83C\uDDEE\uD83C\uDDF6")) {//伊拉克
                    country = "elak";
                }
                if (s1.contains("iran") || s1.contains("\uD83C\uDDEE\uD83C\uDDF7")) {//伊朗
                    country = "elang";
                }
                if (s1.contains("bangladesh") || s1.contains("\uD83C\uDDE7\uD83C\uDDE9")) {//孟加拉国
                    country = "mangj";
                }
                if (s1.contains("saudi") || s1.contains("arabia") || s1.contains("\uD83C\uDDF8\uD83C\uDDE6")) {//沙特阿拉伯
                    country = "shate";
                }
                if (s1.contains("georgia") || s1.contains("\uD83C\uDDEC\uD83C\uDDEA")) {//格鲁吉亚
                    country = "gelu";
                }
                if (s1.contains("russian") || s1.contains("\uD83C\uDDF7\uD83C\uDDFA")) {//俄罗斯
                    country = "eluos";
                }
                if (s1.contains("italy") || s1.contains("italian") || s1.contains("rome") || s1.contains("\uD83C\uDDEE\uD83C\uDDF9")) {
                    country = "eteli";
                    break;
                }
                if (s1.contains("türkiye") || s1.contains("turkey") || s1.contains("turkiye") || s1.contains("\uD83C\uDDF9\uD83C\uDDF7")) {//土耳其
                    country = "tuerq";
                }
                if (s1.contains("chad") || s1.contains("\uD83C\uDDF9\uD83C\uDDE9")) {//乍得
                    country = "zhade";
                }
                if (s1.contains("uzbekistan") || s1.contains("\uD83C\uDDFA\uD83C\uDDFF")) {//乌兹别克斯坦
                    country = "wuzi";
                }
                if (s1.contains("canada") || s1.contains("\uD83C\uDDE8\uD83C\uDDE6")) {//加拿大
                    country = "jianada";
                }
                if (s1.contains("ukraine") || s1.contains("\uD83C\uDDFA\uD83C\uDDE6")) {//乌克兰
                    country = "wukelan";
                }

                if (s1.contains("france") || s1.contains("frenchman") || s1.contains("french") || s1.contains("paris") || s1.contains("\uD83C\uDDEB\uD83C\uDDF7")) {
                    country = "fr";
                    break;
                }


                if (s1.contains("germany") || s1.contains("german") || s1.contains("germen") || s1.contains("berlin") || s1.contains("\uD83C\uDDE9\uD83C\uDDEA")) {
                    country = "de";
                    break;
                }


                if (s1.contains("malaysia") || s1.contains("malaysian") || s1.contains("malay") || s1.contains("kuala") || s1.contains("\uD83C\uDDF2\uD83C\uDDFE")) {
                    country = "ms";
                    break;
                }
                if (s1.contains("singapore") || s1.contains("singapores") || s1.contains("\uD83C\uDDF8\uD83C\uDDEC")) {
                    country = "en_si";
                    break;
                }
            }


            if (!country.equals("")) {

            } else {



                country = "fail";
            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG + from_id, "eee  122---->" + CrashHandler.getInstance().printCrash(e));
        }
        LoggerUtil.logI(TAG + from_id, "country 128----->" + country);
        WriteFileUtil.write(country, Global.COUNTRY_JUDGE + from_id);
        return country;
    }
}
