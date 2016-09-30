package com.organization.sjhg.e_school.Fragments;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.Helpers.QuestionAnswerAdapter;
import com.organization.sjhg.e_school.ListStructure.QuestionAnswerList;
import com.organization.sjhg.e_school.ListStructure.QuestionList;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Utils.Latex_Image_Loader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/17/2016.
 */
public class Question_Answer_Fragment extends Fragment {

    List<QuestionAnswerList> questionAnswerLists=new ArrayList<>();

    public  Question_Answer_Fragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=this.getArguments();
        this.questionAnswerLists= (List<QuestionAnswerList>) bundle.getSerializable(getString(R.string.sendlist));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.question_answer_fragment, container, false);
        ImageView imageView=(ImageView)rootView.findViewById(R.id.question_image);
        final TextView question_text=(TextView)rootView.findViewById(R.id.question_text);
        TextView time_taken=(TextView)rootView.findViewById(R.id.time_taken);
        TextView answer_text=(TextView)rootView.findViewById(R.id.answer_text);
        TextView user_option_text=(TextView)rootView.findViewById(R.id.user_option_text);
        ImageView imageView1=(ImageView)rootView.findViewById(R.id.solution_image);

        String url= ServerAddress.getServerAddress(getContext())+questionAnswerLists.get(0).question_image;
        imageLoader(url,imageView);
        String url1=ServerAddress.getServerAddress(getContext())+questionAnswerLists.get(0).solution_path;
        imageLoader(url1,imageView1);
        time_taken.setText("Time Taken : "+questionAnswerLists.get(0).time_taken);

        String code=questionAnswerLists.get(0).question_text;
        textLoader(code,question_text);
       String  code1=questionAnswerLists.get(0).correctoption.get(0).name;
        textLoader(code1,answer_text);
        if(!questionAnswerLists.get(0).response.equals("empty"))
        {
            String code2=questionAnswerLists.get(0).useroption.get(0).name;
            textLoader(code2,user_option_text);
        }

        return rootView;
    }

    private void imageLoader(String url,ImageView imageView)
    {
        Picasso.with(getContext())
                .load(url)
                .resize(200,200)
                .into(imageView);
    }

    private void textLoader(String code, final TextView textView)
    {
        Spanned spanned = Html.fromHtml(code, new Html.ImageGetter() {
            // download latex symbol
            @Override
            public Drawable getDrawable(String source) {
                LevelListDrawable d = new LevelListDrawable();
                Drawable empty = getContext().getResources().getDrawable(R.drawable.ic_launcher);
                d.addLevel(0, 0, empty);
                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                new Latex_Image_Loader().execute(source, d,textView);
                return d;
            }
        }, null);

        textView.setText(spanned);

    }
}
