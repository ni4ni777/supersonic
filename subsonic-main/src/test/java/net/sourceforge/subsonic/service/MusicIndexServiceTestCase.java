/*
 This file is part of Subsonic.

 Subsonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Subsonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Subsonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 (C) Sindre Mehus
 */
package net.sourceforge.subsonic.service;

import junit.framework.TestCase;
import net.sourceforge.subsonic.domain.MusicIndex;

import java.util.Arrays;
import java.util.List;

import org.powermock.reflect.Whitebox;

/**
 * Unit test of {@link MusicIndex}.
 *
 * @author Sindre Mehus
 */
public class MusicIndexServiceTestCase extends TestCase {

    private final MusicIndexService musicIndexService = new MusicIndexService();

    public void testCreateIndexFromExpression() throws Exception {
        MusicIndex index = musicIndexService.createIndexFromExpression("A");
        assertEquals("A", index.getIndex());
        assertEquals(1, index.getPrefixes().size());
        assertEquals("A", index.getPrefixes().get(0));

        index = musicIndexService.createIndexFromExpression("The");
        assertEquals("The", index.getIndex());
        assertEquals(1, index.getPrefixes().size());
        assertEquals("The", index.getPrefixes().get(0));

        index = musicIndexService.createIndexFromExpression("X-Z(XYZ)");
        assertEquals("X-Z", index.getIndex());
        assertEquals(3, index.getPrefixes().size());
        assertEquals("X", index.getPrefixes().get(0));
        assertEquals("Y", index.getPrefixes().get(1));
        assertEquals("Z", index.getPrefixes().get(2));
    }

    public void testCreateIndexesFromExpression() throws Exception {
        List<MusicIndex> indexes = musicIndexService.createIndexesFromExpression("A B  The X-Z(XYZ)");
        assertEquals(4, indexes.size());

        assertEquals("A", indexes.get(0).getIndex());
        assertEquals(1, indexes.get(0).getPrefixes().size());
        assertEquals("A", indexes.get(0).getPrefixes().get(0));

        assertEquals("B", indexes.get(1).getIndex());
        assertEquals(1, indexes.get(1).getPrefixes().size());
        assertEquals("B", indexes.get(1).getPrefixes().get(0));

        assertEquals("The", indexes.get(2).getIndex());
        assertEquals(1, indexes.get(2).getPrefixes().size());
        assertEquals("The", indexes.get(2).getPrefixes().get(0));

        assertEquals("X-Z", indexes.get(3).getIndex());
        assertEquals(3, indexes.get(3).getPrefixes().size());
        assertEquals("X", indexes.get(3).getPrefixes().get(0));
        assertEquals("Y", indexes.get(3).getPrefixes().get(1));
        assertEquals("Z", indexes.get(3).getPrefixes().get(2));
    }

    public void testIgnoreNonAlphanumericWhenSorting() throws Exception {
    	MusicIndexService musicIndexService = new MusicIndexService();
    	List<String> artistNames = Arrays.asList("artist", "_artist", " artist", "_art_ist_");
    	for (String artistName : artistNames) {
    		String sortedArtistName = Whitebox.<String> invokeMethod(musicIndexService, "createSortableName", artistName, new String[] {});
    		String expectedArtistName = (artistName.equals("_art_ist_")) ? "art ist" : "artist";
    		assertEquals("artist Name \"" + artistName + "\" became \"" + sortedArtistName + "\"", expectedArtistName, sortedArtistName);
    	}
    }
}