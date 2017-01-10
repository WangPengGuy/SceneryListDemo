package com.android.wangpeng.scenerylistdemo;

/**
 * Created by Mr_wang on 2016/12/19.
 */

public class ProvinceCityID {

    public static int[] getProvinceID(){
        int[] provinceID = {3,27,10,23,19,18,15,12,25,16,31,2,4,17,22,11,13,14,6,7,9,32,26,8,30,28,24,5,21,20,29,50};
        return provinceID;
    }

    public static int[][] getCityID(){

        int[] BeiJing = {53};
        int[] TianJin = {27};
        int[] HeBei = {146,147,145,142,148,139,149,141,140,144,143};
         int[] ShanXiEast = {307,301,309,300,302,306,303,310,308,304,305};
         int[] NeiMeng = {264,261,19,262,266,263,265,19,268,270,19,259};
         int[] LiaoNing = {256,248,245,250,246,249,253,258,251,254,255,257,247,252};
         int[] JinLin = {214,215,217,15,219,213,15,4569,220};
         int[] HeiLongJiang = {170,177,173,12,12,168,180,174,12,175,172,12,12};
         int[] ShangHai = {321};
         int[] JiangSu = {224,229,230,221,226,225,223,222,231,232,233,228,227};
         int[] ZheJiang= {383,388,391,385,384,389,386,393,392,390,387};
         int[] AnHui = {42,50,37,44,47,2,49,36,45,40,41,48,46,52,39,51};
         int[] FuJian= {54,61,58,60,59,62,56,55,57};
         int[] JiangXi= {239,237,240,238,242,244,235,236,243,234,241};
         int[] ShanDong= {287,292,299,298,285,297,296,288,294,295,293,289,291,284,290,283,286};
         int[] HeNan = {163,154,155,157,150,151,160,153,167,162,166,158,156,159,161,164,165,};
         int[] HuBei = {192,184,189,195,181,185,196,186,183,194,190,182,188};
         int[] HuNan = {199,211,205,201,204,209,198,210,207,200,208,202,203,206};
         int[] GuangDong = {80,90,91,97,88,79,83,94,85,95,82,86,89,81,92,87,78,96,77,84,93};
         int[] GuangXi= {108,107,102,110,99,101,109,103,111,98,105,104,106,100};
         int[] HaiNan = {127,133,9,137};
         int[] ChongQing = {394};
         int[] SiChuan= {324,341,336,342,326,333,329,337,335,330,334,332,339,328,325,338,323,327,322,327,331};
         int[] GuiZhou = {114,8,120,112,113,119,118,116,117};
         int[] YunNan = {373,377,381,367,382,374,378,30,368,372,379,380,369,370,30,371};
         int[] XiZang = {28,28,28,28,28,28,28};
         int[] ShanXiWest = {317,315,312,318,316,319,313,24,311,314};
         int[] GanSu = {69,66,5,63,74,75,76,72,68,73,64,71,70,65};
         int[] QingHai = {281,21,276,21,278,21,21,21,};
         int[] NingXia = {274,272,273,271,3105};
         int[] XinJiang= {364,359,29,356,355,29,353,351,29,358,29,366,29,352,361,};
         int[] Others = {33,34,35,100};

        int[][] cityID =
                {
                        BeiJing ,TianJin, HeBei, ShanXiEast, NeiMeng, LiaoNing, JinLin, HeiLongJiang, ShangHai, JiangSu, ZheJiang, AnHui, FuJian, JiangXi,
                        ShanDong, HeNan, HuBei, HuNan, GuangDong, GuangXi, HaiNan, ChongQing, SiChuan, GuiZhou, YunNan, XiZang, ShanXiWest, GanSu, QingHai, NingXia,
                        XinJiang, Others
                };
        return cityID;
    }

}
