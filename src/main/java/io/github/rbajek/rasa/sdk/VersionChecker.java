package io.github.rbajek.rasa.sdk;

import io.github.rbajek.rasa.sdk.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafał Bajek
 */
class VersionChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionChecker.class);

    public static final String SUPPORTED_VERSION = "1.4.0";

    /**
     * <p>Check if the version of rasa and rasa_sdk are compatible.</p>
     *
     * <p>The version check relies on the version string being formatted as
     * 'x.y.z' and compares whether the numbers x and y are the same for both
     * rasa and rasa_sdk.</p>
     *
     * <p>Currently, only warning is logging</p>
     *
     * @param rasaVersion A string containing the version of rasa that is making the call to the action server.
     */
    public static void checkVersionCompatibility(String rasaVersion) {

        // check for versions of Rasa that are too old to report their version number
        if(StringUtils.isNullOrEmpty(rasaVersion)) {
            LOGGER.warn("You are using an old version of rasa which might not be compatible with this version of rasa_sdk ({}).\n" +
                            "To ensure compatibility use the same version for both, modulo the last number, i.e. using version A.B.x " +
                            "the numbers A and B should be identical for both rasa and rasa_sdk.", SUPPORTED_VERSION);
            return;
        }

        String[] rasa = rasaVersion.split("\\.");
        String[] sdk = SUPPORTED_VERSION.split("\\.");

        if(rasa[0].equals(sdk[0])  == false || rasa[1].equals(sdk[1]) == false) {
            LOGGER.warn("Your versions of rasa and rasa_sdk might not be compatible. You are currently running rasa version {} " +
                            "and rasa_sdk version {}.\nTo ensure compatibility use the same version for both, modulo the last number, " +
                            "i.e. using version A.B.x the numbers A and B should be identical for both rasa and rasa_sdk.", rasaVersion, SUPPORTED_VERSION );
        }
    }
}
