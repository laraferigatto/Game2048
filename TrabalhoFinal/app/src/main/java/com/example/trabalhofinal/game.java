package com.example.trabalhofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.example.trabalhofinal.R.drawable.centoevinte;
import static com.example.trabalhofinal.R.drawable.tranparente;

public class game extends AppCompatActivity {
    Map<String, String> mapaDeCores = new HashMap<String, String>();
    ImageView replay;
    ImageView replayGameOver;
    ImageView homeGameover;
    ProgressBar progressBar;
    ImageView desabilitaSom;
    int quantidadeDeClicks = 0;
    TextView scoreGameOver;
    int vida = 100;
    Button btnDestino;
    Button btnClicado;
    //lara
    TextView recordeAtual;
    ImageView pause, play, home, semSom, help;
    TableLayout tableLayout;
    RelativeLayout layoutFimDeJogo;
    int pontos = 0, pontosMax = 0;
    AdView mAdView;
    TextView scoreMax;
    MediaPlayer somJuntoNumeros;
    private InterstitialAd mInterstitialAd;
    Button btn;
    //fimlara
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_game);
        replay = (ImageView) findViewById(R.id.replay);

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout table = findViewById(R.id.tableLayoutPrincipal);
                View child = null;
                for (int i = 0; i < table.getChildCount(); i++) {
                    TableRow tableRow = (TableRow) table.getChildAt(i);
                    for (int j = 0; j < tableRow.getChildCount(); ++j) {
                        child = tableRow.getChildAt(j);
                        Button btn = (Button)child;
                        btn.setText("");
                        btn.setBackgroundResource(android.R.drawable.btn_default);
                        btn.setVisibility(i == 0 ? View.INVISIBLE : View.VISIBLE);
                    }


                }
                popularGrid();
                inicializar();
            }
        });

        desabilitaSom = findViewById(R.id.semSom);
        somJuntoNumeros = MediaPlayer.create(game.this,R.raw.juntou);
        replayGameOver = findViewById(R.id.replayGameOver);
        homeGameover = findViewById(R.id.homeGameover);
        scoreGameOver = findViewById(R.id.scoreAtual);
        layoutFimDeJogo = findViewById(R.id.layoutGameOver);
        progressBar = findViewById(R.id.progressbar);
        recordeAtual = findViewById(R.id.recorde);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        home = findViewById(R.id.home);
        tableLayout = findViewById(R.id.layoutPause);


        scoreMax = findViewById(R.id.pontos);
        SharedPreferences prefs = getSharedPreferences("recorde", MODE_PRIVATE);
        pontosMax = prefs.getInt("recorde", 0);
        scoreMax.setText("Top Score: " + pontosMax);
        progressBar.setProgress(100);

        popularGrid();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        desabilitaSom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                somJuntoNumeros.stop();
            }
        });
        homeGameover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutFimDeJogo.setVisibility(View.INVISIBLE);
                Intent i = new Intent(game.this, MainActivity.class);
                i.putExtra("recorde", String.valueOf(pontos));
                startActivity(i);
                finish();
            }
        });
        replayGameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutFimDeJogo.setVisibility(View.INVISIBLE);
                TableLayout table = findViewById(R.id.tableLayoutPrincipal);
                View child = null;
                for (int i = 0; i < table.getChildCount(); i++) {
                    TableRow tableRow = (TableRow) table.getChildAt(i);
                    for (int j = 0; j < tableRow.getChildCount(); ++j) {
                        child = tableRow.getChildAt(j);
                        Button btn = (Button)child;
                        btn.setText("");
                        btn.setBackgroundResource(android.R.drawable.btn_default);
                        btn.setVisibility(i == 0 ? View.INVISIBLE : View.VISIBLE);
                    }
                }
                popularGrid();
                inicializar();

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout table = findViewById(R.id.tableLayoutPrincipal);
                table.setVisibility(View.INVISIBLE);
                tableLayout.setVisibility(View.VISIBLE);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout table = findViewById(R.id.tableLayoutPrincipal);
                table.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.INVISIBLE);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(game.this, MainActivity.class);
                i.putExtra("pontMax", String.valueOf(pontosMax));
                startActivity(i);
            }
        });


        //larafim



    }
    public void inicializar()
    {
        pontos = 0;
        vida = 100;
        recordeAtual.setText("Score: " + String.valueOf(pontos));
        progressBar.setProgress(vida);
        quantidadeDeClicks = 0;
        btnDestino = null;
        btnClicado = null;
    }

    public void popularGrid() {
        for (int coluna = 1; coluna <= 6; coluna++) {
            Random gerador = new Random();
            int min = 3;
            int max = 8;
            int alturaDeCadaColuna = gerador.nextInt((max - min) + 1) + min;
            Button btnAnterior = null;
            int n = 9 - alturaDeCadaColuna;
            for (int i = 0; i <= n; i++) {
                int linha = alturaDeCadaColuna + i;
                String nomeBtnAtual = "btn" + linha + "-" + coluna;
                Button btnAtual = getButton(nomeBtnAtual);
                //flag para não gerar igual ao de cima
                gerarNumeroButton(gerador, i, btnAtual, btnAnterior );
                btnAnterior = btnAtual;
            }


        }
    }

    public void gerarNumeroButton(Random gerador, int i, Button btnAtual, Button btnAnterior )
    {
        boolean flag = true;
        while (i > 0 && flag) {
            int numero = gerador.nextInt((7 - 1) + 1) + 1;
            switch (numero) {
                case 1:
                    btnAtual.setText("2");
                    coloriBtn(btnAtual);
                    break;
                case 2:
                    btnAtual.setText("4");
                    coloriBtn(btnAtual);
                    break;
                case 3:
                    btnAtual.setText("8");
                    coloriBtn(btnAtual);
                    break;
                case 4:
                    btnAtual.setText("16");
                    coloriBtn(btnAtual);
                    break;
                case 5:
                    btnAtual.setText("32");
                    coloriBtn(btnAtual);
                    break;
                case 6:
                    btnAtual.setText("64");
                    coloriBtn(btnAtual);
                    break;
                case 7:
                    btnAtual.setText("128");
                    coloriBtn(btnAtual);
                    break;
            }
            if (!(btnAnterior.getText().equals(btnAtual.getText()))) {
                flag = false;
            }
        }
    }

    private void coloriBtn(Button btnAtual)
    {
//        cores[0] = "azul";
//        cores[1] = "vermelho";
//        cores[2] = "amarelo";
//        cores[3] = "verde";
//        cores[4] = "magenta";
//        Random gerador = new Random();
//        int i = gerador.nextInt(5);
        String numeroBtn = btnAtual.getText().toString();
        if(mapaDeCores.containsKey(numeroBtn))
        {
            String cor = mapaDeCores.get(numeroBtn);
            btnAtual.setBackgroundColor(Integer.parseInt(cor));
        }
        else
        {
            Random c = new Random();
            int color = Color.rgb(c.nextInt(256),c.nextInt(256),c.nextInt(256));
            mapaDeCores.put(numeroBtn, String.valueOf(color));
            btnAtual.setBackgroundColor(color);

        }





    }

    public Button getButton(String nomeBtn) {
        TableLayout table = findViewById(R.id.tableLayoutPrincipal);
        View child = null;
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow tableRow = (TableRow) table.getChildAt(i);
            for (int j = 0; j < tableRow.getChildCount(); ++j) {
                child = tableRow.getChildAt(j);
                String nameChild = getResources().getResourceEntryName(child.getId());
                if (nameChild.equals(nomeBtn)) {
                    return (Button) child;
                }
            }
        }
        return null;
    }
    public void onClickBtnButons(View v) {
        Button button = (Button) v;
        quantidadeDeClicks++;
        if(button.getText().toString().equals("") && quantidadeDeClicks == 2)
        {
            button =  verificaTopo(button);
            int id = button.getId();
            String nameId = getResources().getResourceEntryName(id);
            String[] posicao = nameId.split("-");
            posicao[0] = posicao[0].replaceAll("btn", "");
            String numeroButton = button.getText().toString();
            int linha = Integer.parseInt(posicao[0]);
            if(linha == 9 && button.getText().toString().equals("")) {
                quantidadeDeClicks = 0;
                button.setText(btnDestino.getText().toString());
                coloriBtn(button);
                btnClicado.setBackgroundResource(android.R.drawable.btn_default);
                btnClicado.setText("");
                btnClicado.setVisibility(View.VISIBLE);
                btnDestino.setText("");
                btnDestino.setBackgroundResource(android.R.drawable.btn_default);
                btnDestino.setVisibility(View.INVISIBLE);
                return;
            }
        }
        button =  verificaTopo(button);

        int id = button.getId();
        String nameId = getResources().getResourceEntryName(id);
        String[] posicao = nameId.split("-");
        posicao[0] = posicao[0].replaceAll("btn", "");
        String numeroButton = button.getText().toString();


        if (quantidadeDeClicks == 1) {
            if(vida > 20)
                vida -= 20;
            progressBar.setProgress(vida);


            btnDestino = getButton("btn" + "0-" + posicao[1]);
            btnDestino.setVisibility(View.VISIBLE);
            btnDestino.setText(numeroButton);
            int cor = ((ColorDrawable)button.getBackground()).getColor();
            btnDestino.setBackgroundColor(cor);
            button.setVisibility(View.INVISIBLE);
            btnClicado = button;

        } else {
            quantidadeDeClicks = 0;
            if(posicao[0].equals("0"))
            {
                vida += 20;
                progressBar.setProgress(vida);
                btnClicado.setText(((Button)v).getText().toString());
                btnClicado.setVisibility(View.VISIBLE);
                button.setVisibility(View.INVISIBLE);
                quantidadeDeClicks = 0;
            }
            else
            {
                String numeroButtonAnterior = btnDestino.getText().toString();
                if (numeroButton.equals(numeroButtonAnterior)) {
                    if(button.getVisibility() == View.INVISIBLE)
                    {
                        vida += 20;
                        progressBar.setProgress(vida);
                        button.setVisibility(View.VISIBLE);
                        btnDestino.setVisibility(View.INVISIBLE);
                        quantidadeDeClicks = 0;
                        return;
                    }
                    vida += 20;
                    progressBar.setProgress(vida);
                    int intTextBtnDestino = Integer.parseInt(numeroButtonAnterior);
                    int intTextBtnLocal = Integer.parseInt(numeroButton);
                    int soma = intTextBtnDestino + intTextBtnLocal;
                    atualizaPontos(soma);
                    button.setText(String.valueOf(soma));
                    coloriBtn(button);
                    somJuntoNumeros.start();
                    createButton(button, button.getText().toString(),true);
                    btnDestino.setText("");
                    btnDestino.setBackgroundResource(android.R.drawable.btn_default);
                    btnDestino.setVisibility(View.INVISIBLE);
                    btnClicado.setVisibility(View.VISIBLE);
                    btnClicado.setText("");
                    btnClicado.setBackgroundResource(android.R.drawable.btn_default);
                    somaButons(button, posicao[0], posicao[1]);

                } else {
                    if(Integer.parseInt(button.getText().toString()) < Integer.parseInt(btnClicado.getText().toString()))
                    {
                        Toast.makeText(game.this, "Voce não pode selecionar números menores!", Toast.LENGTH_SHORT).show();
                        quantidadeDeClicks++;
                        vida += 20;
                        progressBar.setProgress(vida);
                    }
                    else
                    {
                        vida -= 20;
                        progressBar.setProgress(vida);
                        if(vida == 0)
                            gameOver();
                        else {
                            if (createButton(btnClicado, numeroButton, true) &&
                                    createButton(button, btnDestino.getText().toString(), false)) {
                                btnClicado.setText("");
                                btnClicado.setBackgroundResource(android.R.drawable.btn_default);
                                btnClicado.setVisibility(View.VISIBLE);
                                btnDestino.setVisibility(View.INVISIBLE);
                            } else {
                                gameOver();
                            }
                        }
                    }
                }
            }
        }
    }

    private void gameOver()
    {
        layoutFimDeJogo.setVisibility(View.VISIBLE);
        scoreGameOver.setText("Score:" + String.valueOf(pontos));
    }

    private void atualizaPontos(int p) {
        pontos += p;
        recordeAtual.setText("Score: " + String.valueOf(pontos));
        if(pontos > pontosMax) {
            pontosMax = pontos;
            scoreMax.setText("Recorde: " + pontosMax);
        }
        //salvar o record, mas antes tem que salvar os pontos
        SharedPreferences prefs = getSharedPreferences("recorde", MODE_PRIVATE);
        SharedPreferences.Editor edt = prefs.edit();
        edt.putInt("recorde", pontosMax);
        edt.apply();
    }

    private boolean createButton(Button btn, String numero, boolean flag)
    {
        int idBtn = btn.getId();
        String nameIdBtn = getResources().getResourceEntryName(idBtn);
        String[] posicaoBtn = nameIdBtn.split("-");
        posicaoBtn[0] = posicaoBtn[0].replaceAll("btn", "");
        if(posicaoBtn[0] == "1")
        {
            //game over
            return false;
        }
        else
        {
            int linha = Integer.parseInt(posicaoBtn[0]);
            Random gerador = new Random();

            if(flag)
            {
                //gera coluna
                while(true) {
                    int colunaBtnAtual = Integer.parseInt(posicaoBtn[1]);
                    int coluna = gerador.nextInt((6 - 1) + 1) + 1;
                    while (colunaBtnAtual == coluna)
                        coluna = gerador.nextInt((6 - 1) + 1) + 1;

                    //butão da ultila linha
                    Button btnNovo = getButton("btn" + "9" + "-" + String.valueOf(coluna));
                    //botão do topo
                    Button btnTopo = verificaTopo(btnNovo);
                    if (btnTopo.getText().toString().equals("") || btnTopo.getVisibility() == View.INVISIBLE) {
                        Button btnaux = new Button(this);
                        btnaux.setText("0");
                        gerarNumeroButton(gerador, 1, btnTopo, btnaux);
                    } else {
                        int numeroTopo = Integer.parseInt(btnTopo.getText().toString());
                        int idBtnNovo = btnTopo.getId();
                        String nameIdBtnNovo = getResources().getResourceEntryName(idBtnNovo);
                        String[] posicaoBtnNovo = nameIdBtnNovo.split("-");
                        posicaoBtnNovo[0] = posicaoBtnNovo[0].replaceAll("btn", "");
                        int linhaNovo = Integer.parseInt(posicaoBtnNovo[0]);
                        linhaNovo--;
                        btnNovo = getButton("btn" + String.valueOf(linhaNovo) + "-" + posicaoBtnNovo[1]);
                        gerarNumeroButton(gerador, 1, btnNovo, btnTopo);
                        if(Integer.parseInt(btnNovo.getText().toString()) <= 16 )
                        {
                            return true;
                        }
                        else
                        {
                            btnNovo.setBackgroundResource(android.R.drawable.btn_default);
                            btnNovo.setText("");
                        }
                    }
                }
            }
            else {
                linha--;
                Button btnNovo = getButton("btn" + String.valueOf(linha) + "-" + posicaoBtn[1]);
                btnNovo.setText(numero);
                coloriBtn(btnNovo);
            }
        }
        return true;
    }

    private Button verificaTopo(Button button)
    {
        int id = button.getId();
        String nameId = getResources().getResourceEntryName(id);
        String[] posicao = nameId.split("-");
        posicao[0] = posicao[0].replaceAll("btn", "");

        if(posicao[0].equals("0"))
            return button;
        int linha = 1;
        Button btnAux = getButton("btn" + String.valueOf(linha) + "-" + posicao[1]);
        while(linha <= 9 && (btnAux.getText().equals("")))
        {
            linha++;
            if(linha <= 9)
                btnAux = getButton("btn" + String.valueOf(linha) + "-" + posicao[1]);
        }
        return btnAux;
    }


    public void somaButons(Button btnLocal, String linhaString, String coluna)
    {
        int linha = Integer.parseInt(linhaString);
        linha++;
        while(linha <= 9)
        {
            String nameButton = "btn" + linha + "-" + coluna;
            Button btnDeBaixo = getButton(nameButton);
            String textBtnDeBaixo = btnDeBaixo.getText().toString();
            String textBtnLocal = btnLocal.getText().toString();
            if(textBtnDeBaixo.equals(textBtnLocal))
            {
                if(vida < 100) {
                    vida += 20;
                    progressBar.setProgress(vida);
                }
                somJuntoNumeros.start();
                int intTextBtnDeBaixo = Integer.parseInt(textBtnDeBaixo);
                int intTextBtnLocal = Integer.parseInt(textBtnLocal);
                int somaBtn = intTextBtnDeBaixo + intTextBtnLocal;
                atualizaPontos(somaBtn);
                btnLocal.setText("");
                btnLocal.setBackgroundResource(android.R.drawable.btn_default);
                btnDeBaixo.setText(String.valueOf(somaBtn));
                coloriBtn(btnDeBaixo);
                btnLocal = btnDeBaixo;
            }
            else
            {
//                btnLocal.setText("");
//                btnLocal.setVisibility(View.INVISIBLE);
                linha = 10;
            }
            linha++;

        }
    }


}