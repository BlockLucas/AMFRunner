package helper.resource.loader;

import org.raml.v2.api.loader.FileResourceLoader;
import org.raml.v2.api.loader.ResourceLoader;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.isNullOrEmpty;

public class RamlParserExchangeDependencyResourceLoader implements ResourceLoader {

    private String basePath = ".";
    private File workingDir = null;
    private ResourceLoader resourceLoader = null;

    private static final Pattern DEPENDENCY_PATH_PATTERN = Pattern.compile("^exchange_modules/|/exchange_modules/");

    public RamlParserExchangeDependencyResourceLoader(){
        basePath = ".";
        workingDir = new File(basePath);
        resourceLoader = new FileResourceLoader(basePath);
    }

    public RamlParserExchangeDependencyResourceLoader(String rootDir){
        basePath = rootDir != null ? rootDir : ".";
        workingDir = new File(basePath);
        resourceLoader = new FileResourceLoader(basePath);
    }

    @Nullable
    @Override
    public InputStream fetchResource(String path) {
        if (isNullOrEmpty(path)) {
            return null;
        }

        final String resourceName;

        final Matcher matcher = DEPENDENCY_PATH_PATTERN.matcher(path);
        if (matcher.find()) {

            final int dependencyIndex = path.lastIndexOf(matcher.group(0));
            resourceName = dependencyIndex <= 0 ? path : path.substring(dependencyIndex);
            return resourceLoader.fetchResource(new File(workingDir, resourceName).getAbsolutePath());

        } else {
            return null;

        }
    }
}