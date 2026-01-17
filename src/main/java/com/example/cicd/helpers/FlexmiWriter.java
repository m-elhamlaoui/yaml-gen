package com.example.cicd.helpers;

import com.example.cicd.dto.*;

public class FlexmiWriter {

    public static String generateFlexmi(PipelineInput input) {
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<?nsuri input?>\n");
        xml.append("<PipelineInput pipelineName=\"").append(escape(input.getPipelineName())).append("\">\n");

        xml.append("  <jobs>\n");
        for (Job job : input.getJobs()) {
            xml.append("    <Job name=\"").append(escape(job.getName())).append("\"");
            if (job.getRunsOn() != null) {
                xml.append(" runsOn=\"").append(escape(job.getRunsOn())).append("\"");
            }
            xml.append(">\n");

            if (! job.getEnvVariables().isEmpty()) {
                xml.append("      <envVariables>\n");
                for (EnvVariable env : job.getEnvVariables()) {
                    xml.append("        <EnvVariable key=\"").append(escape(env.getKey()))
                            .append("\" value=\"").append(escape(env.getValue())).append("\"/>\n");
                }
                xml.append("      </envVariables>\n");
            }

            if (!job. getNeeds().isEmpty()) {
                xml.append("      <needs>\n");
                for (String need : job.getNeeds()) {
                    xml.append("        ").append(escape(need)).append("\n");
                }
                xml.append("      </needs>\n");
            }

            xml.append("    </Job>\n");
        }
        xml.append("  </jobs>\n");

        xml. append("  <events>\n");
        for (Event event : input.getEvents()) {
            xml.append("    <Event eventType=\"").append(escape(event.getEventType())).append("\">\n");

            if (!event.getBranches().isEmpty()) {
                xml.append("      <branches>\n");
                for (Branch branch : event.getBranches()) {
                    xml.append("        <Branch name=\"").append(escape(branch.getName())).append("\"/>\n");
                }
                xml.append("      </branches>\n");
            }

            xml.append("    </Event>\n");
        }
        xml.append("  </events>\n");

        xml.append("</PipelineInput>\n");

        return xml.toString();
    }

    private static String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                . replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}