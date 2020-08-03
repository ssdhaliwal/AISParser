# AISParser
AIS Message Parser 1.0.0

Requirements for AIS
- Support messages with Tag-block and non-tag-blocks.
- Support messages with multiple fragments.
- Support linking VSI/VDL info to messages.
- Support easy maintenance for updates when message specs are changed.
- Provide event based notifications for Complete, Error, or Update (ie: VSI/VDL)
- Support high-throughput; approx 500k messages or more per min.
- Support all ITU-R M.1371-5 Messages (1..27)
- Support all GNSS data reporting formats for Type 17 as per Recommendation ITU-R M.823
