package com.perenc.xh.commonUtils.utils.baidu;

import com.alibaba.fastjson.JSONArray;

public class RandomCoupletsUtil {

    /**
     * 获取随机对联
     * @return
     */
    public static String getRandomCp() {
        String random = "";
        //String context = "{\"first\": \"+first+\", \"second\": \"+second+\", \"center\": \"+center+\"}";
        String str = "{\"result\":\"success\",\"message\":\"成功！\"}";
        String jsonc1 = "{\"first\":\"大顺大财大吉利\",\"second\":\"新春新喜新世纪\",\"center\":\"万事如意\"}";
        String jsonc2 = "{\"first\":\"精耕细作丰收岁\",\"second\":\"勤俭持家有余年\",\"center\":\"国强富民\"}";
        String jsonc3 = "{\"first\":\"创大业千秋昌盛\",\"second\":\"展宏图再就辉煌\",\"center\":\"大展宏图\"}";
        String jsonc4 = "{\"first\":\"一帆风顺吉星到\",\"second\":\"万事如意福临门\",\"center\":\"财源广进\"}";
        String jsonc5 = "{\"first\":\"春雨丝丝润万物\",\"second\":\"红梅点点绣千山\",\"center\":\"春意盎然\"}";
        String jsonc6 = "{\"first\":\"欢天喜地度佳节\",\"second\":\"张灯结彩迎新春\",\"center\":\"家庭幸福\"}";
        String jsonc7 = "{\"first\":\"大顺大财大吉利\",\"second\":\"新春新喜新世纪\",\"center\":\"万事如意\"}";
        String jsonc8 = "{\"first\":\"旧岁又添几个喜\",\"second\":\"新年更上一层楼\",\"center\":\"辞旧迎新\"}";
        String jsonc9 = "{\"first\":\"岁通盛世家家富\",\"second\":\"人遇年华个个欢\",\"center\":\"皆大欢喜\"}";
        String jsonc10 = "{\"first\":\"迎新春江山锦绣\",\"second\":\"辞旧岁事泰辉煌\",\"center\":\"春意盎然\"}";

        String jsonc11 = "{\"first\":\"一帆风顺年年好\",\"second\":\"万事如意步步高\",\"center\":\"吉星高照\"}";
        String jsonc12 = "{\"first\":\"人来人往皆笑脸\",\"second\":\"店内店外都欢迎\",\"center\":\"国泰民安\"}";
        String jsonc13 = "{\"first\":\"欢声笑语贺新春\",\"second\":\"欢聚一堂迎新年\",\"center\":\"合家欢乐\"}";
        String jsonc14 = "{\"first\":\"和顺门第增百福\",\"second\":\"合家欢乐纳千祥\",\"center\":\"欢度春节\"}";
        String jsonc15 = "{\"first\":\"绿竹别其三分景\",\"second\":\"红梅正报万家春\",\"center\":\"春回大地\"}";
        String jsonc16 = "{\"first\":\"一年四季行好运\",\"second\":\"八方财宝进家门\",\"center\":\"合家欢乐\"}";
        String jsonc17 = "{\"first\":\"春临大地百花艳\",\"second\":\"勤俭持家有余年\",\"center\":\"国强富民\"}";
        String jsonc18 = "{\"first\":\"一帆风顺年年好\",\"second\":\"万事如意步步高\",\"center\":\"五福临门\"}";
        String jsonc19 = "{\"first\":\"高居宝地财兴旺\",\"second\":\"福照家门富生辉\",\"center\":\"心想事成\"}";
        String jsonc20 = "{\"first\":\"岁通盛世家家富\",\"second\":\"人遇年华个个欢\",\"center\":\"皆大欢喜\"}";


        String jsonc21 = "{\"first\":\"迎新春江山锦绣\",\"second\":\"辞旧岁事泰辉煌\",\"center\":\"春意盎然\"}";
        String jsonc22 = "{\"first\":\"财源滚滚随春到\",\"second\":\"喜气洋洋伴福来\",\"center\":\"财源广进\"}";
        String jsonc23 = "{\"first\":\"东风化雨山山翠\",\"second\":\"政策归心处处春\",\"center\":\"春风化雨\"}";
        String jsonc24 = "{\"first\":\"和顺一门有百福\",\"second\":\"平安二字值千金\",\"center\":\"万象更新\"}";
        String jsonc25 = "{\"first\":\"大地流金万事通\",\"second\":\"冬去春来万象新\",\"center\":\"欢度春节\"}";
        String jsonc26 = "{\"first\":\"红梅含苞傲冬雪\",\"second\":\"绿柳吐絮迎新春\",\"center\":\"欢度春节\"}";
        String jsonc27 = "{\"first\":\"春风入喜财入户\",\"second\":\"岁月更新福满门\",\"center\":\"新春大吉\"}";
        String jsonc28 = "{\"first\":\"一干二净除旧习\",\"second\":\"五讲四美树新风\",\"center\":\"辞旧迎春\"}";
        String jsonc29 = "{\"first\":\"多劳多得人人乐\",\"second\":\"丰产丰收岁岁甜\",\"center\":\"形势喜人\"}";
        String jsonc30 = "{\"first\":\"日日财源顺意来\",\"second\":\"年年福禄随春到\",\"center\":\"新春大吉\"}";

        String jsonc31 = "{\"first\":\"家过小康欢乐日\",\"second\":\"春回大地艳阳天\",\"center\":\"人心欢畅\"}";
        String jsonc32 = "{\"first\":\"冬去山川齐秀丽\",\"second\":\"喜来桃里共芬芳\",\"center\":\"新年大吉\"}";
        String jsonc33 = "{\"first\":\"民安国泰逢盛世\",\"second\":\"风调雨顺颂华年\",\"center\":\"民泰国安\"}";
        String jsonc34 = "{\"first\":\"万事如意展宏图\",\"second\":\"心想事成兴伟业\",\"center\":\"五福临门\"}";
        String jsonc35 = "{\"first\":\"内外平安好运来\",\"second\":\"合家欢乐财源进\",\"center\":\"吉星高照\"}";

        String jsonc36 = "{\"first\":\"春归大地人间暖\",\"second\":\"福降神州喜临门\",\"center\":\"福喜盈门\"}";
        String jsonc37 = "{\"first\":\"壮丽山河多异彩\",\"second\":\"文明国度遍高风\",\"center\":\"山河壮丽\"}";
        String jsonc38 = "{\"first\":\"欢天喜地度佳节\",\"second\":\"张灯结彩迎新春\",\"center\":\"家庭幸福\"}";
        String jsonc39 = "{\"first\":\"和顺门第增百福\",\"second\":\"合家欢乐纳千祥\",\"center\":\"欢度春节\"}";
        String jsonc40 = "{\"first\":\"五湖四海皆春色\",\"second\":\"万水千山尽得辉\",\"center\":\"万象更新\"}";

        String jsonc41 = "{\"first\":\"喜居宝地千年旺\",\"second\":\"福照家门万事兴\",\"center\":\"喜迎新春\"}";
        String jsonc42 = "{\"first\":\"日出江花红胜火\",\"second\":\"春来江水绿如蓝\",\"center\":\"鸟语花香\"}";
        String jsonc43 = "{\"first\":\"高居宝地财兴旺\",\"second\":\"福照家门富生辉\",\"center\":\"心想事成\"}";
        String jsonc44 = "{\"first\":\"大地流金万事通\",\"second\":\"冬去春来万象新\",\"center\":\"欢度春节\"}";
        String jsonc45 = "{\"first\":\"迎新春事事如意\",\"second\":\"接鸿福步步高升\",\"center\":\"好事临门\"}";
        String jsonc46 = "{\"first\":\"财源滚滚随春到\",\"second\":\"喜气洋洋伴福来\",\"center\":\"财源广进\"}";
        String jsonc47 = "{\"first\":\"冬去山川齐秀丽\",\"second\":\"喜来桃里共芬芳\",\"center\":\"新年大吉\"}";
        String jsonc48 = "{\"first\":\"一帆风顺年年好\",\"second\":\"万事如意步步高\",\"center\":\"吉星高照\"}";
        String jsonc49 = "{\"first\":\"一年四季春常在\",\"second\":\"万紫千红永开花\",\"center\":\"喜迎新春\"}";
        String jsonc50 = "{\"first\":\"高居宝地财兴旺\",\"second\":\"福照家门富生辉\",\"center\":\"心想事成\"}";

        String jsonc51 = "{\"first\":\"占天时地利人和\",\"second\":\"取九州四海财宝\",\"center\":\"财源不断\"}";
        String jsonc52 = "{\"first\":\"一年四季春常在\",\"second\":\"万紫千红永开花\",\"center\":\"喜迎新春\"}";
        String jsonc53 = "{\"first\":\"五湖四海皆春色\",\"second\":\"万水千山尽得辉\",\"center\":\"万象更新\"}";
        String jsonc54 = "{\"first\":\"迎新春事事如意\",\"second\":\"接鸿福步步高升\",\"center\":\"好事临门\"}";
        String jsonc55 = "{\"first\":\"迎新春事事如意\",\"second\":\"接鸿福步步高升\",\"center\":\"好事临门\"}";
        String jsonc56 = "{\"first\":\"绿竹别其三分景\",\"second\":\"红梅正报万家春\",\"center\":\"春回大地\"}";
        String jsonc57 = "{\"first\":\"欢声笑语贺新春\",\"second\":\"欢聚一堂迎新年\",\"center\":\"新年大吉\"}";
        String jsonc58 = "{\"first\":\"一帆风顺年年好\",\"second\":\"万事如意步步高\",\"center\":\"合家欢乐\"}";
        String jsonc59 = "{\"first\":\"百年天地回元气\",\"second\":\"一统山河际太平\",\"center\":\"国泰民安\"}";
        String jsonc60 = "{\"first\":\"天增岁月人增寿\",\"second\":\"春满乾坤福满楼\",\"center\":\"四季长安\"}";

        String jsonc61 = "{\"first\":\"迎喜迎春迎富贵\",\"second\":\"接财接福接平安\",\"center\":\"吉祥如意\"}";
        String jsonc62 = "{\"first\":\"福旺财旺运气旺\",\"second\":\"家兴人兴事业兴\",\"center\":\"喜气盈门\"}";
        String jsonc63 = "{\"first\":\"春雨丝丝润万物\",\"second\":\"红梅点点绣千山\",\"center\":\"春意盎然\"}";
        String jsonc64 = "{\"first\":\"事事如意大吉祥\",\"second\":\"家家顺心永安康\",\"center\":\"四季兴隆\"}";
        String jsonc65 = "{\"first\":\"春风入喜财入户\",\"second\":\"岁月更新福满门\",\"center\":\"新春大吉\"}";
        String jsonc66 = "{\"first\":\"一年好运随春到\",\"second\":\"四季彩云滚滚来\",\"center\":\"万事如意\"}";
        String jsonc67 = "{\"first\":\"丹凤呈祥龙献瑞\",\"second\":\"红桃贺岁杏迎春\",\"center\":\"福满人间\"}";
        String jsonc68 = "{\"first\":\"天地和顺家添财\",\"second\":\"平安如意人多福\",\"center\":\"四季平安\"}";
        String jsonc69 = "{\"first\":\"年年顺景则源广\",\"second\":\"岁岁平安福寿多\",\"center\":\"吉星高照\"}";
        String jsonc70 = "{\"first\":\"欣看江山万里秀\",\"second\":\"喜听长征一路歌\",\"center\":\"国泰民安\"}";


        String jsonc71 = "{\"first\":\"戌年引导小康路\",\"second\":\"亥岁迎来锦绣春\",\"center\":\"吉祥如意\"}";
        String jsonc72 = "{\"first\":\"犬过千秋留胜迹\",\"second\":\"猪肥万户示丰年\",\"center\":\"喜气盈门\"}";
        String jsonc73 = "{\"first\":\"吉日生财猪拱户\",\"second\":\"新春纳福鹊登梅\",\"center\":\"春意盎然\"}";
        String jsonc74 = "{\"first\":\"两年半夜分新旧\",\"second\":\"万众齐欢接亥春\",\"center\":\"四季兴隆\"}";
        String jsonc75 = "{\"first\":\"国泰民安戌岁乐\",\"second\":\"粮丰财茂亥春兴\",\"center\":\"国泰民安\"}";
        String jsonc76 = "{\"first\":\"人逢盛世情无限\",\"second\":\"猪拱华门岁有余\",\"center\":\"万事如意\"}";
        String jsonc77 = "{\"first\":\"丰稔岁中猪领赏\",\"second\":\"新台阶上步登高\",\"center\":\"福满人间\"}";
        String jsonc78 = "{\"first\":\"犬过千秋留胜迹\",\"second\":\"亥年跃马奔小康\",\"center\":\"四季平安\"}";
        String jsonc79 = "{\"first\":\"巧剪窗花猪拱户\",\"second\":\"妙裁锦绣燕迎春\",\"center\":\"吉星高照\"}";
        String jsonc80 = "{\"first\":\"朱门北启新春色\",\"second\":\"朱门北启新春色\",\"center\":\"国泰民安\"}";

        String jsonc81 = "{\"first\":\"朱红春帖千门瑞\",\"second\":\"翠绿柳风万户新\",\"center\":\"吉祥如意\"}";
        String jsonc82 = "{\"first\":\"朱笔题名振雁塔\",\"second\":\"绿风摇柳动莺声\",\"center\":\"喜气盈门\"}";
        String jsonc83 = "{\"first\":\"名题雁塔登金榜\",\"second\":\"猪拱华门报吉祥\",\"center\":\"春意盎然\"}";
        String jsonc84 = "{\"first\":\"衣丰食足戌年乐\",\"second\":\"国泰民安亥岁欢\",\"center\":\"四季兴隆\"}";
        String jsonc85 = "{\"first\":\"戌岁乘龙立宏志\",\"second\":\"猪肥万户示丰年\",\"center\":\"国泰民安\"}";

        String jsonc86 = "{\"first\":\"国泰民安戌岁乐\",\"second\":\"粮丰财茂亥春兴\",\"center\":\"万事如意\"}";
        String jsonc87 = "{\"first\":\"金榜题名光耀第\",\"second\":\"喜猪拱户院生财\",\"center\":\"福满人间\"}";
        String jsonc88 = "{\"first\":\"狗岁已赢十段锦\",\"second\":\"猪岁再登百步楼\",\"center\":\"四季平安\"}";
        String jsonc89 = "{\"first\":\"狗问平安随腊去\",\"second\":\"猪生财富报春来\",\"center\":\"吉星高照\"}";
        String jsonc90 = "{\"first\":\"狗年已展千重锦\",\"second\":\"猪岁再登百步楼\",\"center\":\"国泰民安\"}";

        String jsonc91 = "{\"first\":\"金猪拱户门庭富\",\"second\":\"紫燕报春岁月新\",\"center\":\"吉祥如意\"}";
        String jsonc92 = "{\"first\":\"猪多粮足农家富\",\"second\":\"子孝孙贤亲寿高\",\"center\":\"喜气盈门\"}";
        String jsonc93 = "{\"first\":\"猪是财神登万户\",\"second\":\"燕为春使舞千家\",\"center\":\"春意盎然\"}";
        String jsonc94 = "{\"first\":\"猪拱家门春贴画\",\"second\":\"鹿衔寿草福临门\",\"center\":\"四季兴隆\"}";
        String jsonc95 = "{\"first\":\"景象升平开泰运\",\"second\":\"金猪如意获丰财\",\"center\":\"国泰民安\"}";

        String jsonc96 = "{\"first\":\"瑞雪纷飞清玉宇\",\"second\":\"花猪起舞贺新年\",\"center\":\"万事如意\"}";
        String jsonc97 = "{\"first\":\"燕衔喜信春光好\",\"second\":\"猪拱财门幸福长\",\"center\":\"福满人间\"}";
        String jsonc98 = "{\"first\":\"爆竹升天送狗岁\",\"second\":\"春花遍地缀猪年\",\"center\":\"四季平安\"}";
        String jsonc99 = "{\"first\":\"腾龙快马逢新世\",\"second\":\"送犬迎猪贺大年\",\"center\":\"吉星高照\"}";
        String jsonc100 = "{\"first\":\"骚人乐撰新春对\",\"second\":\"墨客欣书亥岁联\",\"center\":\"国泰民安\"}";



        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, jsonc1);
        jsonArray.add(1, jsonc2);
        jsonArray.add(2, jsonc3);
        jsonArray.add(3, jsonc4);
        jsonArray.add(4, jsonc5);
        jsonArray.add(5, jsonc6);
        jsonArray.add(6, jsonc7);
        jsonArray.add(7, jsonc8);
        jsonArray.add(8, jsonc9);
        jsonArray.add(9, jsonc10);
        jsonArray.add(10, jsonc11);

        jsonArray.add(11, jsonc12);
        jsonArray.add(12, jsonc13);
        jsonArray.add(13, jsonc14);
        jsonArray.add(14, jsonc15);
        jsonArray.add(15, jsonc16);
        jsonArray.add(16, jsonc17);
        jsonArray.add(17, jsonc18);
        jsonArray.add(18, jsonc19);
        jsonArray.add(19, jsonc20);
        jsonArray.add(20, jsonc21);

        jsonArray.add(21, jsonc22);
        jsonArray.add(22, jsonc23);
        jsonArray.add(23, jsonc24);
        jsonArray.add(24, jsonc25);
        jsonArray.add(25, jsonc26);
        jsonArray.add(26, jsonc27);
        jsonArray.add(27, jsonc28);
        jsonArray.add(28, jsonc29);
        jsonArray.add(29, jsonc30);
        jsonArray.add(30, jsonc31);

        jsonArray.add(31, jsonc32);
        jsonArray.add(32, jsonc33);
        jsonArray.add(33, jsonc34);
        jsonArray.add(34, jsonc35);
        jsonArray.add(35, jsonc36);
        jsonArray.add(36, jsonc37);
        jsonArray.add(37, jsonc38);
        jsonArray.add(38, jsonc39);
        jsonArray.add(39, jsonc40);
        jsonArray.add(40, jsonc41);

        jsonArray.add(41, jsonc42);
        jsonArray.add(42, jsonc43);
        jsonArray.add(43, jsonc44);
        jsonArray.add(44, jsonc45);
        jsonArray.add(45, jsonc46);
        jsonArray.add(46, jsonc47);
        jsonArray.add(47, jsonc48);
        jsonArray.add(48, jsonc49);
        jsonArray.add(49, jsonc50);
        jsonArray.add(50, jsonc51);

        jsonArray.add(51, jsonc52);
        jsonArray.add(52, jsonc53);
        jsonArray.add(53, jsonc54);
        jsonArray.add(54, jsonc55);
        jsonArray.add(55, jsonc56);
        jsonArray.add(56, jsonc57);
        jsonArray.add(57, jsonc58);
        jsonArray.add(58, jsonc59);
        jsonArray.add(59, jsonc60);
        jsonArray.add(60, jsonc61);

        jsonArray.add(61, jsonc62);
        jsonArray.add(62, jsonc63);
        jsonArray.add(63, jsonc64);
        jsonArray.add(64, jsonc65);
        jsonArray.add(65, jsonc66);
        jsonArray.add(66, jsonc67);
        jsonArray.add(67, jsonc68);
        jsonArray.add(68, jsonc69);
        jsonArray.add(69, jsonc70);
        jsonArray.add(70, jsonc71);

        jsonArray.add(71, jsonc72);
        jsonArray.add(72, jsonc73);
        jsonArray.add(73, jsonc74);
        jsonArray.add(74, jsonc75);
        jsonArray.add(75, jsonc76);
        jsonArray.add(76, jsonc77);
        jsonArray.add(77, jsonc78);
        jsonArray.add(78, jsonc79);
        jsonArray.add(79, jsonc80);
        jsonArray.add(80, jsonc81);

        jsonArray.add(81, jsonc82);
        jsonArray.add(82, jsonc83);
        jsonArray.add(83, jsonc84);
        jsonArray.add(84, jsonc85);
        jsonArray.add(85, jsonc86);
        jsonArray.add(86, jsonc87);
        jsonArray.add(87, jsonc88);
        jsonArray.add(88, jsonc89);
        jsonArray.add(89, jsonc90);
        jsonArray.add(90, jsonc91);

        jsonArray.add(91, jsonc92);
        jsonArray.add(92, jsonc93);
        jsonArray.add(93, jsonc94);
        jsonArray.add(94, jsonc95);
        jsonArray.add(95, jsonc96);
        jsonArray.add(96, jsonc96);
        jsonArray.add(97, jsonc97);
        jsonArray.add(98, jsonc98);
        jsonArray.add(99, jsonc99);
        jsonArray.add(100, jsonc100);

        int index = (int) (Math.random() * jsonArray.size());
        random = jsonArray.get(index).toString();
        return random;
    }

    /**
     * 获取祝福语
     * @return
     */
    public static String getRandomBless() {
        String random = "";
        String[] bless = {"祝您及家人新年快乐、阖家幸福！", "祝大家新年大吉、万事如意！", "祝您及家人心想事成、万事顺意！", "祝大家身体健康、吉祥如意！", "祝您及家人喜事多多、好运连连！"
                , "祝大家新春快乐、猪年大吉！", "祝您及家人财源滚滚、福寿绵长！", "祝大家和和睦睦、平平安安！", "祝您及家人百事顺心，吉星高照！", "祝大家花开富贵、文定吉祥！"};
        int index = (int) (Math.random() * bless.length);
        random =bless[index];
        return random;
    }


    /**
     * 获取诗
     * @return
     */
    public static String getRandomPoem() {
        String random = "";
        String[] poem = {"风卷残云一线通\t落花流水夕阳红\t不知理想今何在\t心有灵犀便是空\t", "东风送暖夜初明\t喜看春来万物荣\t云淡山青新雨后\t时间草绿嫩芽生\t",
                "红尘何处觅知音\t万里天空一鹤临\t两袖清风无俗气\t人间最爱是春心\t", "当知恩爱能忘记\t莫道而今不再逢\t生死轮回天地转\t白头偕老共从容\t",
                "车行千里路无踪\t送客天涯两处逢\t风雨声中寻旧梦\t烟云身外踏归程\t", "此去天涯路几多\t明朝何处可相逢\t良宵美景君须记\t对话当年酒一盅\t",
                "踏遍青山马不前\t夕阳晚照落人间\t归途一路风光好\t鱼戏清波水映天\t", "雁叫长空万里声\t鱼翔浅底水波清\t鸟栖枝杈相思语\t草绿林荫一色晴\t",
                "一曲高山唱到今\t清溪流水伴歌吟\t伯牙琴瑟和弦奏\t华夏知音远世尘\t"};
        int index = (int) (Math.random() * poem.length);
        random =poem[index];
        return random;
    }







//    public static void main(String[] args) throws UnsupportedEncodingException {
//        String rcpSring=getRandomCp();
//        System.out.println("=====sdf====" +rcpSring);
//        // String jsonc1 = "{\"first\":\"内外平安好运来\",\"second\":\"合家欢乐财源进\",\"center:\"吉星高照\"}";
//        com.alibaba.fastjson.JSONObject coupletsd = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parseObject(rcpSring);
//
//        String first=  coupletsd.get("first").toString();
//
//        //System.out.println("=====sdf=first===" + first);
//
//    }
}
