;   Copyright (c) 2015 Edward A. Roualdes. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns fd.cli
  (:require [clojure.tools.cli :refer [parse-opts]]
            [fd.Agencys :as A]
            [clojure.string :as string]
            [clojure.java.io :as io]))

(def cli-options
  [["-a" "--agency AGENCY" "acronym of agency of interest"
    :parse-fn #(string/lower-case (str %))
    :validate [#(contains? A/Agencys (first (string/split % #":"))) "fd doesn't understand agency."]]
   ["-f" "--folder FOLDER" "folder to store agency data, absolute path"
    :parse-fn #(str %)
    :validate [#(.exists (io/file %)) "Folder must exist."]]
   ["-h" "--help" "print this message and exit"]])

(defn usage [options-summary]
  (->> ["\nfd => analysis ready federal data\n"
        "Usage: java -jar fd.jar action -a AGENCY -f FOLDER\n"
        "Actions:"
        "  available\t print list of agencies fd understands"
        "  download\t download agency data"
        "\nArguments:"
        options-summary
        "\nSee http://roualdes.us/docs for more information."]
       (string/join \newline)))

(defn error-msg [error]
  (str "The following errors occurred while parsing your command:\n"
       error))

(defn error-msgs [errors]
  (str "The following errors occurred while parsing your command:\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn get-opts [args]
  (parse-opts args cli-options))