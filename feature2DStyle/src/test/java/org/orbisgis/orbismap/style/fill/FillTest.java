/**
 * Feature2DStyle is part of the OrbisGIS platform
 *
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.fill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.orbisgis.orbismap.style.fill.DensityFill;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class FillTest {

    @Test
    public void testDensityFill() {
        DensityFill df = new DensityFill();
        df.setPercentageCovered(10);
        assertEquals(10.0f, df.getPercentageCovered().getValue());
        df.setPercentageCovered(90);
        assertEquals(90.0f, df.getPercentageCovered().getValue());
        assertEquals(0,df.getGraphics().getNumGraphics());
    }

    @Test
    public void testDensityFillInvalidParameterTest() {
        DensityFill df = new DensityFill();
        Assertions.assertThrows(RuntimeException.class, () -> {
            df.setPercentageCovered(-1);
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            df.setPercentageCovered(200);
        });

        Assertions.assertThrows(RuntimeException.class, () -> {
            df.setHatchesOrientation(200);
        });

        Assertions.assertThrows(RuntimeException.class, () -> {
            df.setHatchesOrientation(-10);
        });
    }

}
