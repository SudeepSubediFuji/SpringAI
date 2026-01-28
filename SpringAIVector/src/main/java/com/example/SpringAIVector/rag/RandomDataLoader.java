package com.example.SpringAIVector.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class RandomDataLoader {

    private final VectorStore vectorStore;
    Logger logger = Logger.getLogger(RandomDataLoader.class.getName());

    public RandomDataLoader(VectorStore vectorStore){
        this.vectorStore=vectorStore;
    }

    @PostConstruct
    public void loadSentencesIntoVectorStore(){
        List<String> sentences = List.of(
                "African Elephant has a long, versatile trunk and large, heat-dissipating ears." ,
                "Peregrine Falcon has incredible diving speeds and sharp talons for hunting." ,
                "Great White Shark has rows of serrated teeth and a highly developed sense of smell.",
                "Honey Bee has a complex social structure and the ability to pollinate flowers.",
                "Giant Panda has a specialized wrist bone that functions like an opposable thumb for gripping bamboo." ,
                "Blue Whale has massive baleen plates for filtering tiny krill from the ocean." ,
                "Arctic Fox has a thick, white winter coat that provides camouflage in the snow.",
                "Common Octopus has eight flexible arms and the ability to change its skin color instantly.",
                "Red Kangaroo has powerful hind legs built for hopping across the Australian outback.",
                "Komodo Dragon has a venomous bite and a keen sense of taste used to track prey.",
                "Galápagos Tortoise has a heavy, protective shell and a remarkably long lifespan.",
                "Empress Penguin has a thick layer of blubber and huddles with others to survive extreme cold.",
                "Platypus has a duck-like bill and the rare ability for a mammal to lay eggs.",
                "Cheetah has a slender body and non-retractable claws designed for high-speed chases.",
                "Sloth has a very slow metabolism and long claws for hanging upside down in trees.",
                "Bald Eagle has exceptional vision and builds massive nests high in the canopy.",
                "Chameleon has independently rotating eyes and a long, sticky tongue for catching insects.",
                "Grizzly Bear has a prominent shoulder hump of muscle used for digging and strength.",
                "Bottlenose Dolphin has a sophisticated sonar system used for underwater navigation.",
                "Giraffe has an extremely long neck and a prehensile tongue for reaching high foliage."
        );

        // map to document -> collector will collect and convert to list
       List<Document> documents = sentences.stream().map(Document::new).collect(Collectors.toList());
       vectorStore.add(documents);
         logger.info("Random sentences loaded into Vector Store");
    }

}
