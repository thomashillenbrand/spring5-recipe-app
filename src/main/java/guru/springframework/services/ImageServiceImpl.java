package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    @Override
    @Transactional
    public void saveImageFile(Long recipeId, MultipartFile file) {

        log.debug("Received a file for saving!");
        try {
            Recipe recipe = recipeRepository.findById(recipeId).get();
            Byte[] imageByteArray = new Byte[file.getBytes().length];
            int i=0;
            for (byte b : file.getBytes()) imageByteArray[i++] = b;
            recipe.setImage(imageByteArray);
            recipeRepository.save(recipe);

        } catch (IOException ex) {
            log.error("Unable to parse file", ex);
            ex.printStackTrace();
        }
    }
}
