import datetime


class _Clock:
    def __init__(self):
        self.__datetime = datetime.datetime

    def now(self) -> datetime:
        return self.__datetime.now()

    def from_timestamp(self, timestamp: float) -> datetime:
        return self.__datetime.fromtimestamp(timestamp)

    def time_delta(self, seconds: float) -> datetime.timedelta:
        return datetime.timedelta(seconds=seconds)
