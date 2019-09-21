package lt.metasite.waste.csv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GitService {

    private static final Logger logger = LoggerFactory.getLogger(GitService.class);
    private static final String GIT_URL = "https://github.com/vilnius/atlieku-tvarkymas";
    private final List<CsvUploadService> serviceList;
    @Value("${git-download.enabled}")
    private boolean enabled;

    public GitService(List<CsvUploadService> services) {
        this.serviceList = services;
    }

    @PostConstruct
    public void checkoutGit() throws IOException {
        if(!enabled){
            return;
        }
//        Path tempDir = Files.createTempDirectory("waste");
        Path tempDir = Path.of("/tmp/waste9631168670229141023");
        try {
            System.out.println("Cloning " + GIT_URL + " into " + tempDir.toString());
//            Git.cloneRepository()
//               .setURI(GIT_URL)
//               .setDirectory(tempDir.toFile())
//               .call();

            Files.walk(tempDir)
                 .filter(f -> f.toString().endsWith(".csv"))
                 .forEach(key -> serviceList.stream()
                                            .filter(i -> key.getFileName()
                                                            .toString()
                                                            .contains(i.getFilePattern()))
                                            .findFirst()
                                            .ifPresent(s -> s.parseFile(key)));

            System.out.println("Completed Cloning");
        } catch (Exception e) {
            System.out.println("Exception occurred while cloning repo");
            e.printStackTrace();
        }

    }
}
