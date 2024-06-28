/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.modbus4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>BatchResults class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class BatchResults<K> {
    private final Map<K, Object> data = new HashMap<>();

    /**
     * <p>addResult.</p>
     *
     * @param key a K object.
     * @param value a {@link java.lang.Object} object.
     */
    public void addResult(K key, Object value) {
        data.put(key, value);
    }

    /**
     * <p>Add result sets in bulk</p>
     * @param results Batch results
     */
    public void addBatchResults(BatchResults<K> results) {
        this.data.putAll(results.data);
    }

    /**
     * <p>getValue.</p>
     *
     * @param key a K object.
     * @return a {@link java.lang.Object} object.
     */
    public Object getValue(K key) {
        return data.get(key);
    }

    /**
     * <p>getIntValue.</p>
     *
     * @param key a K object.
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getIntValue(K key) {
        return (Integer) getValue(key);
    }

    /**
     * <p>getLongValue.</p>
     *
     * @param key a K object.
     * @return a {@link java.lang.Long} object.
     */
    public Long getLongValue(K key) {
        return (Long) getValue(key);
    }

    /**
     * <p>getDoubleValue.</p>
     *
     * @param key a K object.
     * @return a {@link java.lang.Double} object.
     */
    public Double getDoubleValue(K key) {
        return (Double) getValue(key);
    }

    /**
     * <p>getFloatValue.</p>
     *
     * @param key a K object.
     * @return a {@link java.lang.Float} object.
     */
    public Float getFloatValue(K key) {
        return (Float) getValue(key);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return data.toString();
    }
}
