/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package logic.clusterization;


import static java.lang.Math.*;
import static logic.clusterization.MathUtil.*;

public class MySphericalUtil {

    private MySphericalUtil() {}

    /**
     * Returns distance on the unit sphere; the arguments are in radians.
     */
    private static double distanceRadians(double lat1, double lng1, double lat2, double lng2) {
        return arcHav(havDistance(lat1, lat2, lng1 - lng2));
    }

    /**
     * Returns the distance between two points, in meters.
     */
    public static double computeDistanceBetween(double lat1, double lng1, double lat2, double lng2) {
        return distanceRadians(toRadians(lat1), toRadians(lng1),
                toRadians(lat2), toRadians(lng2)) * EARTH_RADIUS;
    }


  
}
