using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models
{
    /// <summary>
    /// Deactive: Chưa có xe
    /// Active: Bị khóa bởi manager
    /// Reserved: Đã được đặt
    /// Nonavailable: Đã có xe
    /// </summary>
    public enum ParkingLotStatus
    {
        Deactive = 0,
        Active = 1,
        Reserved = 2,
        Nonavailable = 3,
    }

    /// <summary>
    /// Trường Status này khác với trường Active, trường Active để xóa một area
    /// Trường Status để manager có thể set Area đó được sử dụng hay tạm thời đóng
    /// - Deactive: Tạm đóng bởi manager
    /// - Active: Được sử dủng
    /// </summary>
    public enum AreaStatus
    {
        Deactive = 0,
        Active = 1,
    }

    public enum TransactionStatus
    {
        Pending = 0,
        Reserved = 1,
        Finished = 2,
        Canceled = 3,
    }

}