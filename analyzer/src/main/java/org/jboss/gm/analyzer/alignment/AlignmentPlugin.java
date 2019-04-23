package org.jboss.gm.analyzer.alignment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jboss.gm.common.alignment.AlignmentModel;
import org.jboss.gm.common.alignment.AlignmentUtils;

/**
 * Results in adding a task with name {@value org.jboss.gm.analyzer.alignment.AlignmentTask#NAME}.
 * It also creates the basic alignment.json file that {@value org.jboss.gm.analyzer.alignment.AlignmentTask#NAME} of each project adds to
 *
 */
public class AlignmentPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // we need to create an empty alignment file at the project root
        // this file will then be populated by the alignment task of each project
        if (project.getRootProject() == project) {
            createInitialAlignmentFile(project);
        }
        project.getTasks().create(AlignmentTask.NAME, AlignmentTask.class);
    }

    private void createInitialAlignmentFile(Project project) {
        project.afterEvaluate(pr -> {
            // these operation need to be performed in afterEvaluate because only then is the group information
            // populated for certain
            AlignmentUtils.writeUpdatedAlignmentModel(project, getInitialAlignmentModel(project));
        });

    }

    private AlignmentModel getInitialAlignmentModel(Project project) {
        final AlignmentModel alignmentModel = new AlignmentModel(project.getGroup().toString(), project.getName());
        final List<AlignmentModel.Module> modules = new ArrayList<>();
        modules.add(new AlignmentModel.Module(project.getName()));
        if (!project.getSubprojects().isEmpty()) {
            modules.addAll(project.getSubprojects().stream()
                    .map(p -> new AlignmentModel.Module(p.getName()))
                    .collect(Collectors.toList()));
        }
        alignmentModel.setModules(modules);
        return alignmentModel;
    }
}
