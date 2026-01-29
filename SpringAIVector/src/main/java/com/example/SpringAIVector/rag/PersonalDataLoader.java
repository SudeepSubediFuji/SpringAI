package com.example.SpringAIVector.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

//@Component
public class PersonalDataLoader {

    private final VectorStore vectorStore;
    Logger logger = Logger.getLogger(PersonalDataLoader.class.getName());

    public PersonalDataLoader(VectorStore vectorStore){
        this.vectorStore=vectorStore;
    }

//    @PostConstruct
    public void loadSentencesIntoVectorStore(){
        List<String> sentences = List.of(
                "African Elephant has a long, versatile trunk and large, heat-dissipating ears." ,
                "Peregrine Falcon has incredible diving speeds and sharp talons for hunting." ,
                "Great White Shark has rows of serrated teeth and a highly developed sense of smell.",
                "Honey Bee has a complex social structure and the ability to pollinate flowers.",
                "Giant Panda has a specialized wrist bone that functions like an opposable thumb for gripping bamboo." ,
                "Blue Whale has massive baleen plates for filtering tiny krill from the ocean." ,
                "Arctic Fox has a thick, white winter coat that provides camouflage in the snow.",
                "Peregrine Falcon has incredible diving speeds and sharp talons for hunting.",
                "Quantum Computers utilize subatomic particles to perform complex calculations at high speeds.",
                "Great Pyramid of Giza was constructed using massive limestone blocks with precise astronomical alignment.",
                "Photosynthesis allows green plants to convert sunlight into chemical energy using chlorophyll.",
                "Blockchain Technology provides a decentralized and transparent ledger for securing digital transactions.",
                "Mount Everest is the highest peak above sea level, located in the Himalayan mountain range.",
                "James Webb Space Telescope uses infrared sensors to observe the earliest stars in the universe.",
                "Human Heart is a muscular organ that pumps blood throughout the body via a network of arteries.",
                "Kyoto Protocol was an international treaty designed to reduce greenhouse gas emissions globally.",
                "Tesla Model S features a high-capacity battery pack and advanced autonomous driving sensors.",
                "Great Barrier Reef is the world's largest coral reef system, supporting immense marine biodiversity."
        );

        // map to document -> collector will collect and convert to list
       List<Document> personalDocuments = sentences.stream().map(Document::new).collect(Collectors.toList());
       vectorStore.add(personalDocuments);
         logger.info("Random sentences loaded into Vector Store");
    }

}
