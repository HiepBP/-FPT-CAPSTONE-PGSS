using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models
{
    /// <summary>
    /// Empty: Chưa có xe
    /// InUse: Đang có xe
    /// Reserved: Đã được đặt
    /// </summary>
    public enum ParkingLotStatus
    {
        Empty = 0,
        InUse = 1,
        Reserved = 2,
    }

    /// <summary>
    /// Trường Status này khác với trường Active, trường Active để xóa một area
    /// Trường Status để manager có thể set Area đó được sử dụng hay tạm thời đóng
    /// - Suspend: Tạm đóng
    /// - Active: Được sử dủng
    /// </summary>
    public enum AreaStatus
    {
        Suspend = 0,
        Active = 1,
    }

    public enum TransactionStatus
    {
        Reserved = 0,
        Finished = 1,
        Canceled = 2,
    }

}