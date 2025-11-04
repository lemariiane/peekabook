package com.example.peekaboo;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class fragment_curiosity extends Fragment implements CuriosityAdapter.CuriosityDialogListener {

    private RecyclerView recyclerView;
    private CuriosityAdapter adapter;
    private List<CuriosityModel> curiosityList;

    public fragment_curiosity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_curiosity, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_curiosity);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        curiosityList = new ArrayList<>();

        loadCuriosities();

        adapter = new CuriosityAdapter(curiosityList, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Função que preenche a lista com curiosidades.
     */
    private void loadCuriosities() {
        curiosityList.add(new CuriosityModel(
                "Quanto tempo um gato passa dormindo?",
                "Gatos são mestres do sono! Eles passam cerca de dois terços de suas vidas dormindo. Isso significa que um gato de 9 anos passou apenas 3 anos acordado, o resto foi pura soneca!"
        ));
        curiosityList.add(new CuriosityModel(
                "Cães têm impressões digitais?",
                "Não nas patas, mas sim no nariz! O padrão de rugas e marcas no nariz de cada cachorro é único e exclusivo, funcionando como uma 'impressão digital' para identificação."
        ));
        curiosityList.add(new CuriosityModel(
                "Por que escovar os dentes do meu pet é tão importante?",
                "Doenças dentárias são extremamente comuns em cães e gatos e podem afetar a saúde de órgãos vitais como coração e rins. A escovação regular é a melhor defesa contra problemas sistêmicos."
        ));
        curiosityList.add(new CuriosityModel(
                "Cães realmente veem em preto e branco?",
                "Isso é um mito! Cães enxergam o mundo em cores, mas não como nós. Eles têm uma visão dicromática, percebendo o mundo principalmente em tons de amarelo, azul e cinza."
        ));
        curiosityList.add(new CuriosityModel(
                "Por que os gatos precisam arranhar?",
                "Arranhar é um comportamento essencial. Eles não fazem isso apenas para afiar as garras, mas também para se alongar e deixar marcas de cheiro (feromônios) nas superfícies, delimitando seu território."
        ));
        // Dentro do método loadCuriosities() em fragment_curiosity.java:

        curiosityList.add(new CuriosityModel(
                "Por que os cães giram antes de deitar?",
                "Este comportamento é um instinto ancestral herdado de seus lobos selvagens. Girar ajudava a achatar a grama ou folhagem para criar uma cama confortável e a afastar insetos ou cobras. Hoje, é apenas um hábito que indica que estão prestes a se acomodar."
        ));
        curiosityList.add(new CuriosityModel(
                "Os cães podem sentir o humor das pessoas?",
                "Sim! Cães são mestres em ler a linguagem corporal e o tom de voz. Eles podem captar mudanças sutis em feromônios (químicos que liberamos), o que os ajuda a entender se estamos felizes, ansiosos ou tristes."
        ));
        curiosityList.add(new CuriosityModel(
                "Qual é a raça de cão mais rápida do mundo?",
                "O Galgo (Greyhound) é amplamente considerado a raça mais rápida, podendo atingir velocidades de até 72 km/h em curtas distâncias, superando até mesmo o limite de velocidade em muitas rodovias."
        ));
        curiosityList.add(new CuriosityModel(
                "Gatos podem sonhar?",
                "Acredita-se que sim. Durante o sono REM (Movimento Rápido dos Olhos), que é a fase onde os humanos sonham, é comum observar que gatos e cães movem as patas ou fazem pequenos sons, sugerindo que estão vivenciando eventos enquanto dormem."
        ));
        curiosityList.add(new CuriosityModel(
                "Por que o bigode dos gatos é tão importante?",
                "O bigode (vibrissas) não é apenas um adorno. Ele é um sensor tátil altamente sensível que ajuda o gato a navegar, medir a largura de passagens e sentir mudanças no ar. Nunca corte o bigode do seu gato, pois isso o desorienta."
        ));
        curiosityList.add(new CuriosityModel(
                "A dieta humana é segura para os pets?",
                "Muitos alimentos humanos são perigosos para cães e gatos. Cebola, alho, chocolate, uvas, passas e o adoçante Xilitol são altamente tóxicos e podem causar falência de órgãos. Mantenha-os longe do seu prato."
        ));
        curiosityList.add(new CuriosityModel(
                "Por que os cães ofegam?",
                "Cães ofegam principalmente para se refrescar. Eles não suam eficientemente pela pele como os humanos. O ofegar ajuda a evaporar a água da língua e das vias respiratórias, regulando sua temperatura corporal."
        ));
        curiosityList.add(new CuriosityModel(
                "Pets precisam de protetor solar?",
                "Sim, especialmente cães e gatos de pelo claro, pelagem fina ou com áreas sem pelo (como pontas das orelhas e barriga). Eles podem sofrer queimaduras solares e têm risco aumentado de câncer de pele."
        ));
        curiosityList.add(new CuriosityModel(
                "Cães e gatos podem ver TV?",
                "Eles podem ver as imagens, mas as percebem de forma diferente. Cães veem mais flashes de luz por segundo do que nós (maior 'taxa de atualização'), e gatos se concentram mais em movimentos rápidos. O conteúdo deve ser dinâmico para prender a atenção deles."
        ));
        curiosityList.add(new CuriosityModel(
                "O que significa o 'ronronar' de um gato?",
                "Embora geralmente indique contentamento e felicidade, o ronronar também pode ser um mecanismo de cura e conforto que o gato usa quando está doente, assustado ou ferido. A frequência da vibração (25 a 150 Hz) ajuda a regenerar ossos e músculos."
        ));
        curiosityList.add(new CuriosityModel(
                "Qual é a idade humana de um cão de um ano?",
                "A velha regra de multiplicar por 7 é imprecisa. O primeiro ano de vida de um cão grande é cerca de 15 anos humanos. O segundo ano adiciona mais 9 anos. Depois, a taxa diminui dependendo do porte e raça."
        ));
        curiosityList.add(new CuriosityModel(
                "É seguro dar ossos de galinha aos cães?",
                "Não! Ossos cozidos de qualquer tipo, especialmente os de galinha, são extremamente perigosos. Eles podem se estilhaçar facilmente, causando obstruções ou perfurações graves no sistema digestivo do seu pet."
        ));
        curiosityList.add(new CuriosityModel(
                "Existe um dia de cão mais feliz?",
                "Pesquisas sugerem que os cães ficam mais felizes quando estão com seus donos. Eles produzem oxitocina (o 'hormônio do amor') quando interagem conosco, especialmente durante brincadeiras e carinhos."
        ));
    }

    @Override
    public void onCuriosityClicked(String title, String fullFact) {
        CuriosityDetailDialogFragment dialog = CuriosityDetailDialogFragment.newInstance(title, fullFact);

        if (isAdded()) {
            dialog.show(getParentFragmentManager(), "CuriosityDetailDialog");
        }
    }
}