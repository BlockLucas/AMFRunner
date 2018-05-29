package helper.resource.loader;

import amf.client.remote.Content;
import amf.client.resource.FileResourceLoader;
import amf.client.resource.ResourceLoader;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AMFExchangeDependencyResourceLoader implements ResourceLoader {
    private File workingDir = null;
    private final FileResourceLoader resourceLoader = new FileResourceLoader();

    private static final Pattern DEPENDENCY_PATH_PATTERN = Pattern.compile("^exchange_modules/|/exchange_modules/");

    public AMFExchangeDependencyResourceLoader(String rootDir) {
        String basePath = rootDir != null ? rootDir : ".";
        workingDir = new File(basePath);
    }

    @Override
    public CompletableFuture<Content> fetch(String path) {
        if (path == null || path.isEmpty()) return fail();

        final Matcher matcher = DEPENDENCY_PATH_PATTERN.matcher(path);
        if (matcher.find()) {

            final int dependencyIndex = path.lastIndexOf(matcher.group(0));
            final String resourceName = dependencyIndex <= 0 ? path : path.substring(dependencyIndex);
            return resourceLoader.fetch(new File(workingDir, resourceName).getAbsolutePath());
        }
        return fail();
    }

    private CompletableFuture<Content> fail() {
        return CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Failed to apply.");
        });
    }
}