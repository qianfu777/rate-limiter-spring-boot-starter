function tryAcquire(key, capacity)
    local token = refreshToken(key, capacity)
    if token > 0 then
        redis.call('incr', key)
        return 1
    else
        return 0
    end
end

function refreshToken(key, capacity)
    local exist = tonumber(redis.call('exists', key))
    if exist == 1 then
        local token = tonumber(redis.call('get', key))
        local timeDiff = getTimeDiff(key)

        token = math.max(capacity, token + timeDiff)
        redis.call('set', key, token)
        return token
    else
        redis.call('set', 0)
        return 0
    end
end

function getTimeDiff(key)
    local timeKey = key + ':TIMESTAMP'
    local now = tonumber(redis.call('TIME')[1])
    local exist = tonumber(redis.call('exists', timeKey))
    if exist == 1 then
        local refreshTime = tonumber(redis.call('get', timeKey))
        redis.call('set', timeKey, now)
        return now - refreshTime
    else
        redis.call('set', timeKey, now)
        return 0
    end
end

return tryAcquire(KEYS[1], KEYS[2])