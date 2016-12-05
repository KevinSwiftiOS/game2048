package com.example.hcnucai.game2048;

        import android.content.Context;
        import android.graphics.Color;
        import android.graphics.Typeface;
        import android.util.Log;
        import android.view.Gravity;
        import android.widget.FrameLayout;
        import android.widget.TextView;

public class Card extends FrameLayout {
    //显示卡片的标签
    private TextView label;
    public Card(Context context) {
        super(context);
        //label设置字体大小和背景颜色
        label = new TextView(getContext());
        label.setTextSize(40);
        label.setTextColor(Color.parseColor("#FFFFFF"));

     //   label.setBackgroundColor((0xffbbada0));
        //设置背景颜色
      // label.setBackgroundColor(0x33ffffff);
        //设置字体在布局中的位置为center
        label.setGravity(Gravity.CENTER);
         //设置居中显示
        LayoutParams lp = new LayoutParams(-1, -1);
       //设置margin  分别是左上右下
        lp.setMargins(10, 10, 0, 0);
        label.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
       //将view增加到layout中
        addView(label, lp);

        setNum(0);
    }


    private int num = 0;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;

        if (num<=0) {
            label.setText("");
            label.setBackgroundColor(0x33ffffff);
        }else{
            //这里setText是一个索引 如果不加""的话 所以需要转换成为字符串类型
            //根据数值来设置不同的值
            label.setText(num+"");
            switch (num){
                case 2:
                    label.setBackgroundColor(Color.parseColor("#EADED1"));
                    break;
                case 4:
                    label.setBackgroundColor(Color.parseColor("#E7D9B9"));
                    break;
                case 8:
                    label.setBackgroundColor(Color.parseColor("#E8A065"));
                    break;
                case 16:
                    label.setBackgroundColor(Color.parseColor("#EB844E"));
                    break;
                case 32:
                    label.setBackgroundColor(Color.parseColor("#E7674A"));
                    break;
                case 64:
                    label.setBackgroundColor(Color.parseColor("#E44628"));
                    break;
                case 128:
                    label.setBackgroundColor(Color.parseColor("#F4C55C"));
                    break;
                case 256:
                    label.setBackgroundColor(Color.parseColor("#EDC451"));
                    break;
                case 512:
                    label.setBackgroundColor(Color.parseColor("#EDBE39"));
                    break;
                case 1024:
                    label.setBackgroundColor(Color.parseColor("#EABF31"));
                    break;
                case 2048:
                    label.setBackgroundColor(Color.parseColor("#EDB826"));
                    break;
                default:break;
            }


        }
    }
    //判断卡片中的参数是否相同 card判断相同时合并要用
    public boolean equals(Card o) {
        return getNum()==o.getNum();
    }

}
