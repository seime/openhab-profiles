/**
 * Copyright (c) 2023 Contributors to the Seime Openhab Addons project
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package no.seime.openhab.transform.profiles.internal.config;

import org.eclipse.jdt.annotation.NonNullByDefault;

import no.seime.openhab.transform.profiles.internal.profiles.RedeliverCommandProfile;

/**
 * Configuration for {@link RedeliverCommandProfile}.
 *
 * @author Arne Seime - Initial contribution
 */
@NonNullByDefault
public class RedeliverCommandProfileConfig {
    public int redeliverDelayMillis = 1000;
    public int numRedeliveries = 1;

    @Override
    public String toString() {
        return "RedeliverCommandProfileConfig{" + "numRedeliveries=" + numRedeliveries + ", redeliverDelayMillis="
                + redeliverDelayMillis + '}';
    }
}
