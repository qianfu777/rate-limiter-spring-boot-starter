local total = 0
for i = 1, KEYS do
    if i == 1 then
        key = KEYS[i]
        local exist = redis.call('exists', key)
        redis.call('incr', key)
        if exist == 1 then
            redis.call('expire', key, 2)
        end
    end
    local count = redis.call('get', KEYS[i])
    total = total + count
end

return total