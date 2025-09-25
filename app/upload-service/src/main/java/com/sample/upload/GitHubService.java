package com.sample.upload;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHContentUpdateResponse;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;

public class GitHubService {
    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);
    private final GitHub github;
    private final GHRepository repository;

    public GitHubService() throws IOException {
        logger.info("Initializing GitHub client...");
        this.github = new GitHubBuilder().withOAuthToken(Config.GITHUB_TOKEN).build();
        this.repository = github.getRepository(Config.GITHUB_OWNER + "/" + Config.GITHUB_REPO);
        logger.info("Successfully connected to repository: {}", repository.getFullName());
    }

    public String uploadFile(String fileName, InputStream fileStream, String commitMessage) {
        try {
            String pathInRepo = Config.GITHUB_TARGET_FOLDER.isEmpty()
                    ? fileName
                    : Config.GITHUB_TARGET_FOLDER + "/" + fileName;

            logger.info("Uploading file '{}' to GitHub path: {}", fileName, pathInRepo);

            GHContentUpdateResponse response = repository.createContent()
                    .path(pathInRepo)
                    .content(fileStream.readAllBytes())
                    .message(commitMessage)
                    .commit();

            GHContent content = response.getContent();

            logger.info("Successfully uploaded and committed file '{}'", fileName);

            String htmlUrl = content.getHtmlUrl();
            return (htmlUrl != null) ? htmlUrl : "URL not available";

        } catch (IOException e) {
            logger.error("Error uploading file to GitHub", e);
            throw new RuntimeException("Failed to upload file to GitHub", e);
        }
    }
}
