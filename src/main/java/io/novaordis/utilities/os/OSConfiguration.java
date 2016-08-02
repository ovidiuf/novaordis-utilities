/*
 * Copyright (c) 2016 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.utilities.os;

/**
 *
 * An instance that give access to underlying O/S configuration values that do not change as long as the system is
 * not rebooted (an example is the memory page size). The instance reads those values at initialization, and then it
 * keeps returned the cached values. Instances implementing this interface can only be obtained through the
 * getConfiguration() method of the OS instance.
 *
 * @see OS#getConfiguration()
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/16
 */
public interface OSConfiguration {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the underlying O/S memory page size, in bytes. On Linux, that value is obtained by running
     * getconf PAGESIZE.
     */
    int getMemoryPageSize();
}
