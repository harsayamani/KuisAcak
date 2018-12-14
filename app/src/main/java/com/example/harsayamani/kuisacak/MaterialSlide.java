package com.example.harsayamani.kuisacak;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class MaterialSlide extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.DeepPink)
                        .buttonsColor(R.color.MediumVioletRed)
                        .image(R.drawable.play_kuis)
                        .title("Mulai Kuis")
                        .description("Untuk memulai kuis klik tombol play \nyang berada di kanan bawah!")
                        .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.OrangeRed)
                .buttonsColor(R.color.DarkOrange)
                .image(R.drawable.jawab_soal)
                .title("Jawab Pertanyaan")
                .description("Klik salah satu opsi jawaban yang tersedia \nuntuk menjawab pertanyaan!")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.Green)
                .buttonsColor(R.color.ForestGreen)
                .image(R.drawable.jawaban_benar)
                .title("Jawaban Benar")
                .description("Jika jawaban benar \nanda mendapatkan tambahan 100 score")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.DarkCyan)
                .buttonsColor(R.color.LightSeaGreen)
                .image(R.drawable.jawaban_salah)
                .title("Jawaban Salah")
                .description("Jika jawaban salah \nanda tidak mendapatkan tambahan score")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.DeepPink)
                .buttonsColor(R.color.MediumVioletRed)
                .image(R.drawable.waktu_habis)
                .title("Waktu Habis")
                .description("Jika waktu untuk anda menjawab habis, \nmaka anda tidak mendapatkan tambahan score")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.OrangeRed)
                .buttonsColor(R.color.DarkOrange)
                .image(R.drawable.exp_point)
                .title("Experience Point")
                .description("Jika anda mnyelesaikan kuis ini, \nmaka anda akan mendapat \n100 point experience guna \nmenaikkan level anda")
                .build());
    }

    @Override
    public void onFinish() {
        Intent intent = new Intent(this, DashboardPemain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
