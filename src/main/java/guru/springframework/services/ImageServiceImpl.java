package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * Created by jt on 7/3/17.
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {


    private final RecipeReactiveRepository recipeRepository;

    public ImageServiceImpl( RecipeReactiveRepository recipeService) {

        this.recipeRepository = recipeService;
    }

    @Override
    @Transactional
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {

        Mono<Recipe> recipeMono = recipeRepository.findById(recipeId)
            .map(recipe -> {
                try {
                    Byte [] byteObjects = new Byte[file.getBytes().length];

                    int i = 0;
                    for (byte b : file.getBytes()) {
                        byteObjects[i++] = b;
                    }

                    recipe.setImage(byteObjects);
                    return recipe;
                } catch (IOException ioE) {
                    ioE.printStackTrace();
                    throw new RuntimeException(ioE);
                }
            });

        recipeRepository.save(recipeMono.block()).block();

        return Mono.empty();

    }
}
