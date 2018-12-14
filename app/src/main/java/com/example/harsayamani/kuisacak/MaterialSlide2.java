package com.example.harsayamani.kuisacak;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class MaterialSlide2 extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.DarkCyan)
                .buttonsColor(R.color.LightSeaGreen)
                .image(R.drawable.cancel)
                .title("Aturan 1")
                .description("Dilarang membuat soal yang \nmenganduk unsur kekerasan dan sara")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.Green)
                .buttonsColor(R.color.ForestGreen)
                .image(R.drawable.warning2)
                .title("Aturan 2")
                .description("Soal yang dibuat harus \nmemiliki satu jawaban benar")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.OrangeRed)
                .buttonsColor(R.color.DarkOrange)
                .image(R.drawable.warning2)
                .title("Aturan 3")
                .description("Soal yang dibuat harus \nbermuatan pengetahuan umum")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.DeepPink)
                .buttonsColor(R.color.MediumVioletRed)
                .image(R.drawable.checked)
                .title("Mulai Membuat Soal")
                .description("Selama mengikuti aturan yang sudah disebutkan, \nanda bebas membuat soal berapapun")
                .build());
    }

    @Override
    public void onFinish() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
