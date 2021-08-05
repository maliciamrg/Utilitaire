package com.malicia.mrg.util;


import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType;
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class Output implements StreamingProcessOwner {

    private static final Logger LOGGER = LogManager.getLogger(Output.class);
    public String resumer = "";
    public String[] resumerString;
    public String[] traceString;
    NumberFormat formatter = new DecimalFormat("00000");
    private final JProgressBar progress;
    private int i = 0;

    public Output(String[] resumerString, String[] traceString, JProgressBar progress) {
        this.resumerString = resumerString;
        this.traceString = traceString;
        this.progress = progress;
    }

    public StreamingProcessOutputType getOutputType() {
        return StreamingProcessOutputType.BOTH;
    }

    public void processOutput(String line, boolean stdout) {
        calculAvancement(line);
        if (stdout) {
            i++;
            String lline = "[" + formatter.format(i) + "] " + line;

            boolean resumeEnd = false;
            for (String element : resumerString) {
                resumeEnd = resumeEnd || line.contains(element);
            }
            boolean traceEnd = false;
            for (String element : traceString) {
                resumeEnd = resumeEnd || line.contains(element);
            }

            if (resumeEnd) {

                LOGGER.info(lline);
                if (resumeEnd) {
                    resumer = resumer + lline + "\n";
                }

            } else {


                if (traceEnd) {
                    LOGGER.info(lline);
                } else {
                    LOGGER.debug(lline);
                }

            }
        } else {
            LOGGER.error(line);
        }
    }

    private void calculAvancement(String line) {
        if (line.contains("-chk=")) {
            String[] res1 = line.split("-chk=");
            String[] nb = res1[1].split("[\\/)]");
            progress.setValue(Integer.valueOf(nb[0]));
            progress.setMaximum(Integer.valueOf(nb[1]));

        }
    }
}